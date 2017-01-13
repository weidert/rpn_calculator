package com.heliomug.calculator.gui;

public enum Mode {
	STANDARD("Standard"),
	COMMAND("Command"),
	MACRO_NAME("Macro Name");
	
	private String name;
	
	private Mode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
