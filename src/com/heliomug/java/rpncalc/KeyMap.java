package com.heliomug.java.rpncalc;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;

public class KeyMap extends Saveable {
	private static final long serialVersionUID = -3288921805242491882L;

	public static final String FILE_DESC = "RPN Keyboard Layout";
	public static final String FILE_EXT = "rpnkeys";

	private Map<KeyStroke, Command> keyToCommand;
	
	private Map<Command, KeyStroke> commandToKey;
	
	public KeyMap() {
		keyToCommand = new HashMap<KeyStroke, Command>();
		commandToKey = new HashMap<Command, KeyStroke>();
	}
	
	// getters, etc
	public Command getCommand(KeyEvent e) { return getCommand(keyStroke(e)); }
	public Command getCommand(KeyStroke ks) { return keyToCommand.get(ks); }
	public Command getCommand(int keyCode, boolean shiftOn) { return keyToCommand.get(keyStroke(keyCode, shiftOn)); }
	public KeyStroke getKeyStroke(Command command) { 
		return commandToKey.get(command);
	}
	public String getKeyStrokeText(Command c) {
		KeyStroke ks = getKeyStroke(c);
		if (ks == null) {
			return "";
		} else {
			String toRet = KeyEvent.getKeyText(ks.getKeyCode());
			if (toRet.equals("Delete")) toRet = "Del";
			if (toRet.equals("Backspace")) toRet = "";
			if (toRet.equals("Page Down")) toRet = "P\u2193";
			if (toRet.equals("Page Up")) toRet = "P\u2191";
			if (toRet.equals("Period")) toRet = ".";
			if (toRet.equals("Comma")) toRet = ",";
			if (toRet.equals("Semicolon")) toRet = ";";
			if (((int)ks.getModifiers() & (int)java.awt.event.InputEvent.SHIFT_DOWN_MASK) == 0) toRet = toRet.toLowerCase();
			toRet = toRet.replaceAll("numpad-", "");
			toRet = toRet.replaceAll("numpad", "");
			return toRet;
		}
	}
	public String toString() { return keyToCommand.toString(); }
	public int getNumberOfCommands(int keyCode) {
		int toRet = 0;
		if (getCommand(keyStroke(keyCode, false)) != null) toRet++;
		if (getCommand(keyStroke(keyCode, true)) != null) toRet++;
		return toRet;
	}
	public String getCommandString(int keyCode, boolean shiftOn) {
		Command command = keyToCommand.get(keyStroke(keyCode, shiftOn));
		return command == null ? "" : command.toString();
	}

	public void addPrimary(KeyStroke ks, Command com) {
		keyToCommand.put(ks, com);
		commandToKey.put(com, ks);
	}
	
	public void addSecondary(KeyStroke ks, Command com) {
		keyToCommand.put(ks, com);
	}
	
	public static KeyMap getStandardKeyMap() {
		KeyMap map = new KeyMap();
		map.setupStandardKeys();
		return map;
	}
	
	// constructors, etc
	private void setupStandardKeys() {
		addPrimary(keyStroke(KeyEvent.VK_F, false), StandardCommand.ADD); 
		addPrimary(keyStroke(KeyEvent.VK_D, false), StandardCommand.MULTIPLY); 
		addPrimary(keyStroke(KeyEvent.VK_A, false), StandardCommand.DIVIDE); 
		addPrimary(keyStroke(KeyEvent.VK_S, false), StandardCommand.SUBTRACT); 

		
		addPrimary(keyStroke(KeyEvent.VK_A, true), StandardCommand.ROUND); 
		addPrimary(keyStroke(KeyEvent.VK_F, true), StandardCommand.COMBO); 
		addPrimary(keyStroke(KeyEvent.VK_G, true), StandardCommand.RAND); 
		addPrimary(keyStroke(KeyEvent.VK_S, true), StandardCommand.MOD); 

		addPrimary(keyStroke(KeyEvent.VK_E, false), StandardCommand.POW); 
		addPrimary(keyStroke(KeyEvent.VK_W, false), StandardCommand.SWAP); 
		addSecondary(keyStroke(KeyEvent.VK_PAGE_DOWN, false), StandardCommand.SWAP); 
		addPrimary(keyStroke(KeyEvent.VK_Q, false), StandardCommand.SQRT); 
		addPrimary(keyStroke(KeyEvent.VK_G, false), StandardCommand.FLIP); 

		addPrimary(keyStroke(KeyEvent.VK_Y, false), StandardCommand.STORE); 

		addPrimary(keyStroke(KeyEvent.VK_E, true), StandardCommand.SQUARE); 
		addPrimary(keyStroke(KeyEvent.VK_W, true), StandardCommand.CYCLE); 
		addPrimary(keyStroke(KeyEvent.VK_Q, true), StandardCommand.ROOT); 
		addPrimary(keyStroke(KeyEvent.VK_D, true), StandardCommand.PERM); 

		addPrimary(keyStroke(KeyEvent.VK_V, false), StandardCommand.SIN); 
		addPrimary(keyStroke(KeyEvent.VK_C, false), StandardCommand.COS); 
		addPrimary(keyStroke(KeyEvent.VK_X, false), StandardCommand.TAN); 

		addPrimary(keyStroke(KeyEvent.VK_V, true), StandardCommand.ASIN); 
		addPrimary(keyStroke(KeyEvent.VK_C, true), StandardCommand.ACOS); 
		addPrimary(keyStroke(KeyEvent.VK_X, true), StandardCommand.ATAN); 

		addPrimary(keyStroke(KeyEvent.VK_0, false), StandardCommand.INSERT_0); 
		addPrimary(keyStroke(KeyEvent.VK_1, false), StandardCommand.INSERT_1); 
		addPrimary(keyStroke(KeyEvent.VK_2, false), StandardCommand.INSERT_2); 
		addPrimary(keyStroke(KeyEvent.VK_3, false), StandardCommand.INSERT_3); 
		addPrimary(keyStroke(KeyEvent.VK_4, false), StandardCommand.INSERT_4); 
		addPrimary(keyStroke(KeyEvent.VK_5, false), StandardCommand.INSERT_5); 
		addPrimary(keyStroke(KeyEvent.VK_6, false), StandardCommand.INSERT_6); 
		addPrimary(keyStroke(KeyEvent.VK_7, false), StandardCommand.INSERT_7); 
		addPrimary(keyStroke(KeyEvent.VK_8, false), StandardCommand.INSERT_8); 
		addPrimary(keyStroke(KeyEvent.VK_9, false), StandardCommand.INSERT_9); 
		addSecondary(keyStroke(KeyEvent.VK_1, true), StandardCommand.FACT); 
		addSecondary(keyStroke(KeyEvent.VK_5, true), StandardCommand.MOD); 
		addSecondary(keyStroke(KeyEvent.VK_6, true), StandardCommand.POW); 
		addSecondary(keyStroke(KeyEvent.VK_8, true), StandardCommand.MULTIPLY); 
		addSecondary(keyStroke(KeyEvent.VK_MINUS, false), StandardCommand.SUBTRACT); 
		addSecondary(keyStroke(KeyEvent.VK_EQUALS, true), StandardCommand.ADD); 
		
		addPrimary(keyStroke(KeyEvent.VK_N, false), StandardCommand.INSERT_0); 
		addPrimary(keyStroke(KeyEvent.VK_M, false), StandardCommand.INSERT_0); 
		addPrimary(keyStroke(KeyEvent.VK_PERIOD, false), StandardCommand.ADD_DECIMAL); 
		addPrimary(keyStroke(KeyEvent.VK_J, false), StandardCommand.INSERT_1); 
		addPrimary(keyStroke(KeyEvent.VK_K, false), StandardCommand.INSERT_2); 
		addPrimary(keyStroke(KeyEvent.VK_L, false), StandardCommand.INSERT_3); 
		addPrimary(keyStroke(KeyEvent.VK_U, false), StandardCommand.INSERT_4); 
		addPrimary(keyStroke(KeyEvent.VK_I, false), StandardCommand.INSERT_5); 
		addPrimary(keyStroke(KeyEvent.VK_O, false), StandardCommand.INSERT_6); 
		
		addPrimary(keyStroke(KeyEvent.VK_H, false), StandardCommand.NEG); 
		
		
		addSecondary(keyStroke(KeyEvent.VK_ENTER, false), StandardCommand.ENTER); 
		addSecondary(keyStroke(KeyEvent.VK_ADD, false), StandardCommand.ADD); 
		addSecondary(keyStroke(KeyEvent.VK_MULTIPLY, false), StandardCommand.MULTIPLY); 
		addSecondary(keyStroke(KeyEvent.VK_DIVIDE, false), StandardCommand.DIVIDE); 
		addSecondary(keyStroke(KeyEvent.VK_SUBTRACT, false), StandardCommand.SUBTRACT);
		addSecondary(keyStroke(KeyEvent.VK_DECIMAL, false), StandardCommand.ADD_DECIMAL);
		addPrimary(keyStroke(KeyEvent.VK_SPACE, false), StandardCommand.ENTER); 
		addPrimary(keyStroke(KeyEvent.VK_SPACE, true), StandardCommand.DROP); 
		addSecondary(keyStroke(KeyEvent.VK_MULTIPLY, true), StandardCommand.POW); 
		addSecondary(keyStroke(KeyEvent.VK_ADD, true), StandardCommand.NEG); 
		addSecondary(keyStroke(KeyEvent.VK_ENTER, true), StandardCommand.DROP); 
		addSecondary(keyStroke(KeyEvent.VK_DIVIDE, true), StandardCommand.FLIP); 
		
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD0, false), StandardCommand.INSERT_0); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD1, false), StandardCommand.INSERT_1); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD2, false), StandardCommand.INSERT_2); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD3, false), StandardCommand.INSERT_3); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD4, false), StandardCommand.INSERT_4); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD5, false), StandardCommand.INSERT_5); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD6, false), StandardCommand.INSERT_6); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD7, false), StandardCommand.INSERT_7); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD8, false), StandardCommand.INSERT_8); 
		addSecondary(keyStroke(KeyEvent.VK_NUMPAD9, false), StandardCommand.INSERT_9); 

		addPrimary(keyStroke(KeyEvent.VK_Z, false), StandardCommand.LN); 
		addPrimary(keyStroke(KeyEvent.VK_Z, true), StandardCommand.EXP); 

		addPrimary(keyStroke(KeyEvent.VK_BACK_SPACE, false), StandardCommand.BACKSPACE);
		addPrimary(keyStroke(KeyEvent.VK_DELETE, false), StandardCommand.CLEAR_ENTRY); 
		addPrimary(keyStroke(KeyEvent.VK_DELETE, true), StandardCommand.CLEAR_STACK); 
		
		addPrimary(keyStroke(KeyEvent.VK_R, false), StandardCommand.MACRO_RUN);
		addPrimary(keyStroke(KeyEvent.VK_T, false), StandardCommand.MACRO_START_STOP);
		addPrimary(keyStroke(KeyEvent.VK_R, true), StandardCommand.MACRO_NAME_START);
		addPrimary(keyStroke(KeyEvent.VK_T, true), StandardCommand.MACRO_DELETE);

		addPrimary(keyStroke(KeyEvent.VK_B, false), StandardCommand.UNDO);
		addPrimary(keyStroke(KeyEvent.VK_SLASH, false), StandardCommand.COMMAND_INPUT_START);
		addPrimary(keyStroke(KeyEvent.VK_SEMICOLON, true), StandardCommand.COMMAND_INPUT_START);
		addPrimary(keyStroke(KeyEvent.VK_PAGE_DOWN, true), StandardCommand.CYCLE);
		addPrimary(keyStroke(KeyEvent.VK_PAGE_UP, false), StandardCommand.REVERSE_CYCLE);
	}

	private static KeyStroke keyStroke(KeyEvent e) {
		return keyStroke(e.getKeyCode(), e.isShiftDown());
	}
	private static KeyStroke keyStroke(int keyCode, boolean shiftOn) {
		return KeyStroke.getKeyStroke(keyCode, shiftOn ? KeyEvent.SHIFT_DOWN_MASK : 0);
	}
	
	
	@Override
	public String getFileDescription() { return FILE_DESC; }
	@Override
	public String getFileExtension() { return FILE_EXT; }
}
