package com.heliomug.java.rpncalc;

import java.io.Serializable;
import java.util.ArrayList;

public class Num implements Serializable {
	private static final long serialVersionUID = 7822169636515479154L;

	private static final int MAX_SIG_FIGS_INTERNAL = 100;  
	private static final int MAX_SIG_FIGS_TO_DISPLAY = 18;  
	private static final int MAX_DIGITS_AFTER_DECIMAL = 20;
	private static final int SIG_FIGS_IN_DOUBLE = 17;
	private static final int FUDGE_DIGITS_IN_A_ROW_THRESHOLD = 4;
	
	private ArrayList<Integer> digits;
	private boolean decimalOn;
	private int base;
	private int pow;
	
	public Num() {
		digits = new ArrayList<Integer>();
		base = 10;
		pow = 0;
		decimalOn = false;
		setDigit(0, 0);
	}
	
	public Num(Num other) {
		this.decimalOn = other.decimalOn;
		this.base = other.base;
		this.pow = other.pow;
		this.digits = new ArrayList<Integer>();
		for (int i = 0 ; i < other.digits.size() ; i++) {
			this.digits.add(other.digits.get(i));
		}
	}
	
	public Num(int n) {
		this();
		setDigit(0, n);
		normalize();
	}
	
	public Num(double d) {
		this();
		boolean isNegative = false;
		if (d < 0) {
			isNegative = true;
			d *= -1;
		}
		
		int pow = (int) Math.ceil(Math.log(d) / Math.log(base));
		for (int i = pow ; i >= pow - SIG_FIGS_IN_DOUBLE ; i--) {
			setDigit(i, (int)((d * Math.pow(base, -i)) % base));
		}
		normalize();
		
		if (isNegative) {
			pm();
		}
	}

	public boolean isInteger() {
		return (minPlace() >= 0);
	}
	
	public int integerPart() {
		int toRet = 0;
		for (int i = 0 ; i <= maxPlace() ; i++) {
			toRet += Math.pow(base, i)*getDigit(i);
		}
		return toRet;
	}
	
	public boolean isNegative() {
		return (getDigit(maxPlace()) < 0);
	}
	
	public boolean isZero() {
		return (pow == 0 && getDigit(0) == 0 & minPlace() == 0 && maxPlace() == 0);
	}

	public double doubleValue() {
		double toRet = 0;
		for (int i = minPlace() ; i <= maxPlace() ; i++) {
			toRet += getDigit(i) * Math.pow(base, i);
		}
		return toRet;
	}

	public int integerValue() {
		return (int) doubleValue();
	}
	
	public int sigFigs() { return maxPlace() - minPlace() + 1; }

	public String getString(String separator, String decimal) {
		if (isNegative()) {
			return "-" + this.negify().toString();
		} else {
			String toRet = "" ;

			boolean sciNotMode = (Math.abs(pow) > MAX_SIG_FIGS_TO_DISPLAY);// || sigFigs() > MAX_SIG_FIGS_TO_DISPLAY); 

			if (!sciNotMode && maxPlace() < 0) {
				toRet += "0.";
				for (int i = -1 ; i > pow ; i--) {
					toRet += "0";
				}
			}
			for (int i = maxPlace() ; (i >= minPlace() || i >= 0) && i >= -1 * MAX_DIGITS_AFTER_DECIMAL ; i--) {
				toRet += getDigit(i);

				if (sciNotMode && i == maxPlace()) toRet += decimal;
				if (!sciNotMode && decimalOn && i == 0) toRet += decimal;
				if (!sciNotMode && i % 3 == 0) {
					if (i > 0) {
						toRet += separator;
					} else if (i < 0) {
						// toRet += " ";
					}
				}
				
				if (sciNotMode && index(i) > MAX_SIG_FIGS_TO_DISPLAY) break;
			}

			if (sciNotMode) {
				toRet += " X " + base + "^" + pow;
			}
			
			return toRet;
		}
	}
	
	@Override 
	public String toString() {
		return getString(",", ".");
	}
	
	public String fullString() {
		String toRet = "";
		toRet += String.format("%10s: %s\n", "digits", digits);
		toRet += String.format("%10s: %d\n", "len", digits.size());
		toRet += String.format("%10s: %b\n", "deciOn", decimalOn);
		toRet += String.format("%10s: %d\n", "pow", pow);
		toRet += String.format("%10s: %d\n", "base", base);
		toRet += String.format("%10s: %d\n", "maxPlace", maxPlace());
		toRet += String.format("%10s: %d\n", "minPlace", minPlace());
		return toRet;
	}
	
	
	private static Num negativeOne() {
		Num toRet = new Num();
		toRet.setDigit(0, -1);
		return toRet;
	}
	
	private static final Num PI() {
		return new Num(Math.PI);
	}
	
	private int index(int place) { return pow - place; }
	private int place(int index) { return pow - index; }
	private int maxPlace() { return place(0); }
	private int minPlace() { return place(digits.size() - 1); }
	
	
	private void fudge() {
		boolean sawAnyDigit = false;
		int zerosInARow = 0;
		int ninesInARow = 0;
		for (int i = 0 ; i >= minPlace() ; i--) {
			if (getDigit(i) == 0) {
				if (sawAnyDigit) zerosInARow++;
			} else { 
				sawAnyDigit = true;
				zerosInARow = 0;
			}
			if (getDigit(i) == 9) {
				ninesInARow++;
			} else {
				ninesInARow = 0;
			}
			
			if (ninesInARow >= FUDGE_DIGITS_IN_A_ROW_THRESHOLD || zerosInARow >= FUDGE_DIGITS_IN_A_ROW_THRESHOLD) {
				round(i);
				break;
			}
		}
		
	}
	
	private void setDigit(int place, int val) {
		if (index(place) > MAX_SIG_FIGS_INTERNAL && place >= minPlace()) {
			digits.set(index(place), 0);
		} else {
			if (place > maxPlace()) {
				int missingDigits = place - maxPlace();
				for (int i = 0 ; i < missingDigits ; i++) {
					digits.add(0, 0);
					pow++;
				}
			} else if (place < minPlace()) {
				int missingDigits = minPlace() - place;
				for (int i = 0 ; i < missingDigits ; i++) {
					digits.add(0);
				}
			}
			digits.set(index(place), val);
			if (minPlace() < 0) decimalOn = true;
		}
	}
	
	private void trim() {
		for (int place = maxPlace() ; place > minPlace() ; place--) {
			if (getDigit(place) == 0) {
				digits.remove(index(place));
				pow--;
			} else {
				break;
			}
		}
		for (int place = minPlace() ; place < maxPlace() ; place++) {
			if (getDigit(place) == 0) {
				digits.remove(index(place));
			} else {
				break;
			}
		}
	}
	
	private int getDigit(int place) {
		if (place > maxPlace() || place < minPlace()) {
			return 0;
		} else {
			return digits.get(index(place));
		}
	}
	
	private void incrementDigit(int place, int val) {
		setDigit(place, getDigit(place) + val);
	}

	private void normalize() {
		for (int i = minPlace() ; i <= maxPlace() ; i++) {
			int dig = getDigit(i);
			if (dig < 0) {
				if (i == maxPlace() && getDigit(i) == -1) {
					// it's negative
				} else {
					while(getDigit(i) < 0) {
						incrementDigit(i, base);
						incrementDigit(i + 1, -1);
					}
				}
			}
			if (dig >= base) {
				int carry = dig / base;
				int stay = dig % base;
				setDigit(i, stay);
				incrementDigit(i + 1, carry);
			}
		}
		trim();
		fudge();
		trim();
	}
	
	private void round(int place) {
		if (getDigit(place - 1) >= base - base/2) {
			incrementDigit(place, 1);
		}			
		//			System.out.println(this);
		for (int i = place - 1 ; i >= minPlace() ; i--) {
			setDigit(i, 0);
		}
		this.normalize();
	}

	private void pm() {
		for (int i = minPlace() ; i <= maxPlace() ; i++) {
			setDigit(i, -1 * getDigit(i));
		}
		normalize();
	}
	
	public Num addDecimalPoint() {
		Num toRet = new Num(this);
		toRet.decimalOn = true;
		return toRet;
	}

	public Num addDigit(int n) {
		Num toRet = new Num(this);
		if (toRet.isNegative()) {
			toRet.pm();
			toRet = toRet.addDigit(n);
			toRet.pm();
		} else {
			if (toRet.isZero() && !toRet.decimalOn) {
				toRet.setDigit(0, n);
			} else {
				if (decimalOn) {
					toRet.setDigit(toRet.minPlace() - 1, n);
				} else {
					toRet.pow++;
					toRet.setDigit(0, n);
				}
			}
		}
		return toRet;
	}

	public Num delDigit() {
		Num toRet = new Num(this);
		// if we're at the decimal point
		if (toRet.decimalOn && toRet.minPlace() == 0) {
			toRet.decimalOn = false;
			return toRet;
		} else if (toRet.sigFigs() == 1 && !toRet.decimalOn) {
			toRet.setDigit(toRet.pow, 0);
			return toRet;
		} else if (toRet.sigFigs() > 1) {
			if (!toRet.decimalOn) toRet.pow--;
			toRet.digits.remove(toRet.index(toRet.minPlace()));
			return toRet;
		}
		return toRet;
	}

	public Num negify() {
		Num toRet = this.copy();
		toRet.pm();
		return toRet;
	}
	
	public Num add(Num other) {
		Num a = (Num)other;
		Num toRet = new Num();
		int maxPlace = Math.max(this.maxPlace(), a.maxPlace());
		int minPlace = Math.min(this.minPlace(), a.minPlace());
		for (int i = minPlace ; i <= maxPlace ; i++) {
			toRet.setDigit(i, this.getDigit(i) + a.getDigit(i));
		}
		toRet.normalize();
		return toRet;
	}

	public Num subtract(Num other) {
		Num neg = (Num)(other.multiply(Num.negativeOne()));
		Num toRet = this.add(neg);
		return toRet;
	}

	public Num divide(Num other) {
		return this.multiply(other.reciprocal());
	}

	public Num multiply(Num other) {
		Num a = (Num) other;
		Num toRet = new Num();
		for (int i = this.minPlace() ; i <= this.maxPlace() ; i++) {
			for (int j = a.minPlace() ; j <= a.maxPlace() ; j++) {
				toRet.incrementDigit(i + j, this.getDigit(i) * a.getDigit(j));
			}
		}
		toRet.normalize();
		return toRet;
	}

	public Num reciprocal() {
		if (this.doubleValue() == 0) {
			return new Num();
		} else {
			return new Num(1.0 / this.doubleValue());
		}
	}

	public Num sqrt() {
		Num toRet = new Num(Math.sqrt(this.doubleValue()));
		return toRet;
	}
	
	public Num pow(Num other) {
		return new Num(Math.pow(this.doubleValue(), other.doubleValue()));
	}
	
	public Num copy() {
		return new Num(this);
	}

	public Num asin() {
		return new Num(Math.asin(this.doubleValue()));
	}

	public Num acos() {
		return new Num(Math.acos(this.doubleValue()));
	}

	public Num atan() {
		return new Num(Math.atan(this.doubleValue()));
	}

	public Num sin() {
		return new Num(Math.sin(this.doubleValue()));
	}

	public Num cos() {
		return new Num(Math.cos(this.doubleValue()));
	}

	public Num tan() {
		return new Num(Math.tan(this.doubleValue()));
	}

	public Num exp() {
		return new Num(Math.exp(this.doubleValue()));
	}
	
	public Num ln() {
		return new Num(Math.log(this.doubleValue()));
	}

	public Num degToRad() {
		return this.multiply(Num.PI()).divide(new Num(180));
	}

	public Num radToDeg() {
		return this.multiply(new Num(180)).divide(Num.PI());
	}
}
