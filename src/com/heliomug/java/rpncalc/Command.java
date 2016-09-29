package com.heliomug.java.rpncalc;

public interface Command {
	boolean run(Calculator c);
	boolean isRecordable();
	String getName();
	String getAbbreviation();
	String getButtonString();
}
