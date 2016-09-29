package com.heliomug.java.rpncalc;

import java.awt.Color;
import java.awt.Font;

public class DisplaySettings extends Saveable {
	private static final long serialVersionUID = -4263111486819847439L;

	public static final String FILE_DESC = "RPN Display Settings";
	public static final String FILE_EXT = "rpndisp";
	
	private static final String DEFAULT_SEPARATOR = ",";
	private static final String DEFAULT_DECIMAL = ".";
	private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 64);
	private static final Color DEFAULT_TEXT_COLOR = Color.WHITE;
	private static final String DEFAULT_TEXT_FONT = Font.MONOSPACED;
	private static final int DEFAULT_TEXT_STYLE = Font.PLAIN;
	private static final int DEFAULT_TEXT_SIZE = 24;
	private static final int DEFAULT_NUM_LABELS = 5;
	private static final boolean DEFAULT_KEYBOARD_VISIBLE = true;
	private static final boolean DEFAULT_QWERTY_VISIBLE = false;
	private static final boolean DEFAULT_MACRO_VISIBLE = false;
	private static final boolean DEFAULT_COMMANDS_VISIBLE = false;
	
	private Color backgroundColor;
	private Color textColor;
	private int textSize;
	private int textStyle;
	private String textFace;
	private String separator;
	private String decimal;
	
	private boolean isKeyboardVisible;
	private boolean isQwertyVisible;
	private boolean isMacroVisible;
	private boolean isCommandsVisible;

	private int numLabels;
	
	public DisplaySettings() {
		backgroundColor = DEFAULT_BACKGROUND_COLOR;
		textColor = DEFAULT_TEXT_COLOR;
		textSize = DEFAULT_TEXT_SIZE;
		textStyle = DEFAULT_TEXT_STYLE;
		textFace = DEFAULT_TEXT_FONT;
		separator = DEFAULT_SEPARATOR;
		decimal = DEFAULT_DECIMAL;
		numLabels = DEFAULT_NUM_LABELS;
		isKeyboardVisible = DEFAULT_KEYBOARD_VISIBLE;
		isQwertyVisible = DEFAULT_QWERTY_VISIBLE;
		isMacroVisible = DEFAULT_MACRO_VISIBLE;
		isCommandsVisible = DEFAULT_COMMANDS_VISIBLE;
	}

	public int getNumLabels() { return this.numLabels; }
	public Color getBackgroundColor() { return this.backgroundColor; }
	public Color getTextColor() { return this.textColor; }
	public int getTextSize() { return this.textSize; }
	public String getTextFace() { return this.textFace; }
	public int getTextStyle() { return this.textStyle; }
	public String getSeparator() { return this.separator; }
	public String getDecimal() { return this.decimal; }
	public String getFileDescription() { return FILE_DESC; }
	public String getFileExtension() { return FILE_EXT; }
	public Font getTextFont() { return new Font(textFace, textStyle, textSize); }
	public boolean isKeyboardVisible() { return this.isKeyboardVisible; }
	public boolean isQwertyVisible() { return this.isQwertyVisible; }
	public boolean isMacroVisible() { return this.isMacroVisible; }
	public boolean isCommandsVisible() { return this.isCommandsVisible; }
	
	public void setKeyboardVisible(boolean b) { this.isKeyboardVisible = b; }
	public void setQwertyVisible(boolean b) { this.isQwertyVisible = b; }
	public void setMacroVisible(boolean b) { this.isMacroVisible = b; }
	public void setCommandsVisible(boolean b) { this.isCommandsVisible = b; }
	public void setBackgroundColor(Color c) { this.backgroundColor = c; }
	public void setTextColor(Color c) { this.textColor = c; }
	public void setTextSize(int size) { this.textSize = size; }
	public void setSeparator(String str) { this.separator = str; }
	public void setDecimal(String str) { this.decimal = str; }
	
	public static DisplaySettings load() {
		Object loadedObject = FileHandler.loadObject(FILE_DESC, FILE_EXT); 
		if (loadedObject != null && loadedObject.getClass() == DisplaySettings.class) {
			DisplaySettings loadedConfig = (DisplaySettings) loadedObject;
			return loadedConfig;
		}
		return null;
	}
}
