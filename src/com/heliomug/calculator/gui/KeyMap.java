package com.heliomug.calculator.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;

import com.heliomug.calculator.Command;

public class KeyMap {
	private static final Map<String, String> replacementMap = new HashMap<>();
	private static final Map<String, String> shiftReplacers = new HashMap<>(); 
	
	{
		replacementMap.put("Slash", "/");
		replacementMap.put("Equals", "=");
		replacementMap.put("Backspace", "\u2190");
		replacementMap.put("Insert", "Ins");
		replacementMap.put("Delete", "Del");
		replacementMap.put("Period", ".");
		replacementMap.put("Comma", ",");
		replacementMap.put("Semicolon", ";");
		replacementMap.put("Page Down", "P\u2193");
		replacementMap.put("Page Up", "P\u2191");
		
		shiftReplacers.put("1", "!");
		shiftReplacers.put("5", "%");
		shiftReplacers.put("6", "^");
		shiftReplacers.put("8", "*");
		shiftReplacers.put("=", "+");
		shiftReplacers.put("/", "?");
	}
	
	
	private static KeyMap theKeyMap;
	
	public static KeyMap getKeyMap() {
		if (theKeyMap == null) {
			theKeyMap = new KeyMap();
		}
		return theKeyMap;
	}
	
	Map<KeyStroke, Command> keyMap;
	Map<Command, KeyStroke> reverseMap;
	
	private KeyMap() {
		keyMap = new HashMap<>();
		reverseMap = new HashMap<>();
		for (KeyMapping mapping : KeyMapping.values()) {
			addMapping(mapping);
		}
	}

	public String getShortcut(Command c) {
		KeyStroke stroke = getStroke(c);
		if (stroke == null) {
			return "";
		} else {
			String toRet = KeyEvent.getKeyText(stroke.getKeyCode());
			
			if (replacementMap.get(toRet) != null) {
				toRet = replacementMap.get(toRet);
			}

			if ((stroke.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0) {
				if (shiftReplacers.get(toRet) != null) {
					toRet = shiftReplacers.get(toRet);
				}
			} else {
				toRet = toRet.toLowerCase();
			}
			toRet = toRet.replaceAll("numpad-", "");
			toRet = toRet.replaceAll("numpad", "");
			return toRet;
		}
	}
	
	public KeyStroke getStroke(Command command) {
		if (command == null) {
			return null;
		} else {
			return reverseMap.get(command);
		}
	}
	
	public void addMapping(KeyStroke stroke, Command com) {
		keyMap.put(stroke, com);
		reverseMap.put(com, stroke);
	}
	
	public void addMapping(int key, boolean withShift, boolean withoutShift, Command com) {
		if (withShift) {
			addMapping(KeyStroke.getKeyStroke(key, InputEvent.SHIFT_DOWN_MASK), com);
		}
		if (withoutShift) {
			addMapping(KeyStroke.getKeyStroke(key, 0), com);
		}
	}
	
	public void addMapping(KeyMapping mapping) {
		addMapping(mapping.getKey(), mapping.isWithShift(), mapping.isWithoutShift(), mapping.getCommand());
	}
	
	public void addMapping(int key, Command com) {
		addMapping(key, true, true, com);
	}
	
	public Command getCommand(KeyStroke stroke) {
		return keyMap.get(stroke);
	}
}
