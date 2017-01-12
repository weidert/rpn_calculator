package com.heliomug.calculator;

public enum AngleMode {
	DEGREES(0, "Degrees", "Deg", "D", 180 / Math.PI),
	RADIANS(1, "Radians", "Rad", "R", 1),
	GRADIANS(2, "Gradians", "Grad", "G", 200 / Math.PI);
	
	private String name;
	private String abbrev;
	private String letter;
	private double factor;
	private int index;
	
	private AngleMode(int index, String name, String abbrev, String letter, double factor) {
		this.index = index;
		this.name = name;
		this.abbrev = abbrev;
		this.letter = letter;
		this.factor = factor;
	}

	public AngleMode next() {
		return AngleMode.values()[(index + 1) % AngleMode.values().length];
	}
	
	public String getName() {
		return name;
	}

	public String getAbbrev() {
		return abbrev;
	}

	public String getLetter() {
		return letter;
	}

	public double getFactor() {
		return factor;
	}
}
