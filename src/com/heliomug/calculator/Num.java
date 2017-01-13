package com.heliomug.calculator;

import java.io.Serializable;

public interface Num extends Serializable {
	Num copy();
	
	boolean isNegative();
	boolean isPositive();
	boolean isZero();
	boolean lessThan(double d);
	boolean greaterThan(double d);
	boolean lessThanEq(double d);
	boolean greaterThanEq(double d);
	
	long getLong();
	double getDouble();
	String toStringWithDec(int places);

	Num integerPart();
	Num fractionalPart();
	Num abs();
	
	Num add(Num other);
	Num mult(Num other);
	Num div(Num other);
	Num sub(Num other);
	
	Num pm();
	Num pow(Num exp);
	Num root(Num exp);
	Num recip();
	Num sqrt();
	
	Num exp();
	Num ln();
	
	Num sin();
	Num cos();
	Num tan();
	Num asin();
	Num acos();
	Num atan();
	
	Num combo(Num other);
	Num perm(Num other);
	Num fact(); 
	Num mod(Num other);
	
	Num addDecimal();
	Num backspace();
	Num addDigit(int dig);
}
