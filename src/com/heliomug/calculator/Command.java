package com.heliomug.calculator;

import java.io.Serializable;

public interface Command extends Serializable {
	boolean apply(Calculator calc);
	String getName();
	String getAbbrev();
	boolean hasName(String name);
	boolean isMacro();
	boolean isRecordable();
	boolean hasPrefix(String prefix); 
}
