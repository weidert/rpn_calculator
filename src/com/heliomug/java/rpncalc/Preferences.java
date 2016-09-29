package com.heliomug.java.rpncalc;

public class Preferences extends Saveable {
	private static final long serialVersionUID = 2530762867346493516L;

	private static final String FILE_DESC = "RPN Preferences";
	private static final String FILE_EXT = "rpnconfig";

	private static final boolean DEFAULT_CONTINUE_FROM_EXIT = false;
		
	private boolean continueFromExit;
	
	public Preferences() {
		continueFromExit = DEFAULT_CONTINUE_FROM_EXIT;
	}
	
	public boolean getContinueFromExit() { return continueFromExit; }
	public String getFileDescription() { return FILE_DESC; }
	public String getFileExtension() { return FILE_EXT; }
	public void setContinueFromExit(boolean b) { continueFromExit = b; }
	
	public static Preferences load() {
		try {
			Preferences prefs = (Preferences) FileHandler.loadHere(".rpnconfig");
			return prefs;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean save() {
		return FileHandler.saveHere(this, ".rpnconfig");
	}
}
