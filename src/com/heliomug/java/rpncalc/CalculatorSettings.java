package com.heliomug.java.rpncalc;

public class CalculatorSettings extends Saveable {
	private static final long serialVersionUID = -6178760399233340262L;

	private static final AngleMode DEFAULT_ANGLE_MODE = AngleMode.DEGREE;

	public static final String FILE_DESC = "RPN Calculator Settings";
	public static final String FILE_EXT = "rpnset";

	private AngleMode angleMode;

	public CalculatorSettings() {
		angleMode = DEFAULT_ANGLE_MODE;
	}

	public AngleMode getAngleMode() { return this.angleMode; }
	public void setAngleMode(AngleMode am) { this.angleMode = am; }
	public void toggleAngleMode() {	angleMode = angleMode.next(); }
	public String getFileDescription() { return FILE_DESC; }
	public String getFileExtension() { return FILE_EXT; }

	enum AngleMode {
		DEGREE(0, "(DEG)   "),
		RADIAN(1, "(RAD)   ");
		
		private int mode;
		private String str;
		
		AngleMode(int n, String str) {
			mode = n;
			this.str = str;
		}
		
		public String toString() { return this.str; }
		
		private AngleMode getAngleMode(int n) {
			for (AngleMode am : AngleMode.values()) {
				if (am.mode == n) {
					return am;
				}
			}
			return null;
		}
		
		public AngleMode next() {
			return getAngleMode((mode + 1) % AngleMode.values().length); 
		}
	}

}
