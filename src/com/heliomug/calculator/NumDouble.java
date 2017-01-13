package com.heliomug.calculator;

public class NumDouble implements Num {
	private static final long serialVersionUID = 1053127743097059926L;

	private static int MAX_AFTER_DEC = 14;
	private static int BASE = 10;
	
	private double value;
	int decimalPlace;
	
	public NumDouble() {
		value = 0;
		decimalPlace = 0;
	}

	public NumDouble(long val) {
		this(val, 0);
	}
	
	public NumDouble(double value) {
		this(value, MAX_AFTER_DEC);
	}
	
	public NumDouble(double value, int decPlace) {
		this.value = value;
		this.decimalPlace = decPlace;
	}
	
	@Override
	public Num copy() {
		return new NumDouble(value);
	}

	
	
	@Override
	public boolean isNegative() {
		return value < 0;
	}

	@Override
	public boolean isPositive() {
		return value > 0;
	}

	@Override
	public boolean isZero() {
		return value == 0.0;
	}
	
	@Override
	public boolean lessThan(double d) {
		return value < d;
	}
	
	@Override
	public boolean greaterThan(double d) {
		return value > d;
	}
	
	@Override
	public boolean lessThanEq(double d) {
		return value <= d;
	}
	
	@Override
	public boolean greaterThanEq(double d) {
		return value >= d;
	}

	
	@Override
	public long getLong() {
		return (long)value;
	}
	
	@Override
	public double getDouble() {
		return value;
	}

	
	
	@Override
	public Num integerPart() {
		return new NumDouble((int)value);
	}

	@Override
	public Num fractionalPart() {
		return new NumDouble(value - (int)value);
	}

	@Override
	public Num abs() {
		return new NumDouble(Math.abs(value));
	}
	
	@Override
	public Num add(Num other) {
		return new NumDouble(value + other.getDouble());
	}

	@Override
	public Num mult(Num other) {
		return new NumDouble(value * other.getDouble());
	}

	@Override
	public Num div(Num other) {
		return new NumDouble(value / other.getDouble());
	}

	@Override
	public Num sub(Num other) {
		return new NumDouble(value - other.getDouble());
	}

	
	
	@Override
	public Num pm() {
		return new NumDouble(-1 * value, decimalPlace);
	}

	@Override
	public Num pow(Num exp) {
		return new NumDouble(Math.pow(value, exp.getDouble()));
	}

	@Override
	public Num root(Num exp) {
		return this.pow(exp.recip());
	}

	@Override
	public Num recip() {
		return new NumDouble(1 / value);
	}

	@Override
	public Num sqrt() {
		return new NumDouble(Math.sqrt(value));
	}

	@Override
	public Num exp() {
		return new NumDouble(Math.exp(value));
	}

	@Override
	public Num ln() {
		return new NumDouble(Math.log(value));
	}

	
	
	@Override
	public Num sin() {
		return new NumDouble(Math.sin(value));
	}

	@Override
	public Num cos() {
		return new NumDouble(Math.cos(value));
	}

	@Override
	public Num tan() {
		return new NumDouble(Math.tan(value));
	}

	@Override
	public Num asin() {
		return new NumDouble(Math.asin(value));
	}

	@Override
	public Num acos() {
		return new NumDouble(Math.acos(value));
	}

	@Override
	public Num atan() {
		return new NumDouble(Math.atan(value));
	}

	
	
	@Override
	public Num combo(Num other) {
		long n = getLong();
		long k = other.getLong();
		long in = 1;
		long ik = 1;
		long ij = 1;
		long result = 1;
		while (in <= n) {
			result *= in;
			if (ik <= k && result % ik == 0) {
				result /= ik;
				ik++;
			}
			if (ij <= (n - k) && result % ij == 0) {
				result /= ij;
				ij++;
			}
			in++;
		}
		while (ik <= k) {
			result /= ik;
			ik++;
		}
		while (ij <= (n - k)) {
			result /= ij;
			ij++;
		}
		return new NumDouble(result);
	}

	@Override
	public Num perm(Num other) {
		long n = getLong();
		long k = other.getLong();
		int in = 1;
		int ik = 1;
		long result = 1;
		while (in <= n) {
			result *= in;
			if (ik <= k && result % ik == 0) {
				result /= ik;
				ik++;
			}
			in++;
		}
		while (ik <= k) {
			result /= ik;
			ik++;
		}
		return new NumDouble(result);
	}

	@Override
	public Num fact() {
		long fact = 1;
		long x = getLong();
		for (int i = 2 ; i <= x ; i++) {
			fact *= i;
		}
		return new NumDouble(fact);
	}

	@Override
	public Num mod(Num other) {
		long mod = getLong() % other.getLong(); 
		return new NumDouble(mod);
	}
	
	
	
	@Override
	public Num addDecimal() {
		if (decimalPlace == 0) {
			return new NumDouble(value, 1);
		} else {
			return this;
		}
	}

	@Override
	public Num backspace() {
		if (decimalPlace == 1) {
			return new NumDouble((int)value, 0);
		} else if (decimalPlace == 0) {
			return new NumDouble((int)(value / BASE));
		} else {
			long factor = (long)Math.pow(BASE, decimalPlace - 2);
			double newVal = (long)(value * factor);
			newVal = newVal / factor; 
			return new NumDouble(newVal, decimalPlace - 1);
		}
	}

	@Override
	public Num addDigit(int dig) {
		int diff = dig * (Math.signum(value) < 0 ? -1 : 1);
		if (decimalPlace == 0) {
			return new NumDouble(value * BASE + diff, 0);
		} else {
			if (decimalPlace < MAX_AFTER_DEC) {
				return new NumDouble(value + diff * Math.pow(BASE, - decimalPlace), decimalPlace + 1);
			}
			return this;
		}
	}

	public String toStringWithDec(int dec) {
		if (value > Long.MAX_VALUE || value < Long.MIN_VALUE) {
			return String.valueOf(value);
		} else {
			String fmt = "%,." + dec + "f";
			return String.format(fmt, value);
		}
	}
	
	public String toString() {
		if (value > Long.MAX_VALUE || value < Long.MIN_VALUE) {
			return String.valueOf(value);
		} else if (decimalPlace == 0) {
			return String.format("%,d", (long)value);
		} else if (decimalPlace == 1) {
			return String.format("%,d.", (long)value);
		} else {
			String fmt = "%,." + (decimalPlace - 1) + "f";
			return String.format(fmt, value);
		}
	}
	
	
	
	public static void maing(String[] args) {
		Num a = new NumDouble(3);
		Num b = new NumDouble(5);
		b = b.addDigit(0);
		System.out.println(a);
		System.out.println(b);
		Num c = b.combo(a);
		System.out.println(c);
	}
}
