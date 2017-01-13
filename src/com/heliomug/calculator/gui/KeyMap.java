package com.heliomug.calculator.gui;

import static com.heliomug.calculator.StandardCommand.*;
import static java.awt.event.KeyEvent.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.KeyStroke;

import com.heliomug.calculator.Command;

public class KeyMap {
	private static final Map<String, String> REPLACEMENT_MAP = new HashMap<>();
	private static final Map<String, String> SHIFT_REPLACEMENT_MAP = new HashMap<>(); 
	
	private static Map<KeyStroke, Command> shortcutMap = new HashMap<>();
	private static Map<Command, KeyStroke> reverseMap = new HashMap<>();
	
	static {
		REPLACEMENT_MAP.put("Slash", "/");
		REPLACEMENT_MAP.put("Equals", "=");
		REPLACEMENT_MAP.put("Backspace", "\u2190");
		REPLACEMENT_MAP.put("Insert", "Ins");
		REPLACEMENT_MAP.put("Delete", "Del");
		REPLACEMENT_MAP.put("Period", ".");
		REPLACEMENT_MAP.put("Comma", ",");
		REPLACEMENT_MAP.put("Semicolon", ";");
		REPLACEMENT_MAP.put("Page Down", "P\u2193");
		REPLACEMENT_MAP.put("Page Up", "P\u2191");
		
		SHIFT_REPLACEMENT_MAP.put("1", "!");
		SHIFT_REPLACEMENT_MAP.put("2", "@");
		SHIFT_REPLACEMENT_MAP.put("3", "#");
		SHIFT_REPLACEMENT_MAP.put("4", "$");
		SHIFT_REPLACEMENT_MAP.put("5", "%");
		SHIFT_REPLACEMENT_MAP.put("6", "^");
		SHIFT_REPLACEMENT_MAP.put("7", "!");
		SHIFT_REPLACEMENT_MAP.put("8", "*");
		SHIFT_REPLACEMENT_MAP.put("9", "(");
		SHIFT_REPLACEMENT_MAP.put("0", ")");
		SHIFT_REPLACEMENT_MAP.put("-", "_");
		SHIFT_REPLACEMENT_MAP.put("=", "+");
		SHIFT_REPLACEMENT_MAP.put("/", "?");
		
		addMapping(VK_ADD, ADD);
		addMapping(VK_MULTIPLY, MULT);
		addMapping(VK_SLASH, DIV);
		addMapping(VK_DIVIDE, DIV);
		addMapping(VK_MINUS, SUB);
		addMapping(VK_SUBTRACT, SUB);
		addMapping(VK_0, PUT_0, false);
		addMapping(VK_1, PUT_1, false);
		addMapping(VK_2, PUT_2, false);
		addMapping(VK_3, PUT_3, false);
		addMapping(VK_4, PUT_4, false);
		addMapping(VK_5, PUT_5, false);
		addMapping(VK_6, PUT_6, false);
		addMapping(VK_7, PUT_7, false);
		addMapping(VK_8, PUT_8, false);
		addMapping(VK_9, PUT_9, false);
		addMapping(VK_1, FACT, true);
		addMapping(VK_3, STORE, true); 
		addMapping(VK_5, MOD, true); 
		addMapping(VK_6, POW, true);
		addMapping(VK_8, MULT, true);
		addMapping(VK_EQUALS, ADD, true);
		addMapping(VK_NUMPAD0, PUT_0);
		addMapping(VK_NUMPAD1, PUT_1);
		addMapping(VK_NUMPAD2, PUT_2);
		addMapping(VK_NUMPAD3, PUT_3);
		addMapping(VK_NUMPAD4, PUT_4);
		addMapping(VK_NUMPAD5, PUT_5);
		addMapping(VK_NUMPAD6, PUT_6);
		addMapping(VK_NUMPAD7, PUT_7);
		addMapping(VK_NUMPAD8, PUT_8);
		addMapping(VK_NUMPAD9, PUT_9);
		addMapping(VK_DECIMAL, PUT_DECIMAL);
		addMapping(VK_PERIOD, PUT_DECIMAL);
		addMapping(VK_BACK_SPACE, BACKSPACE); 
		addMapping(VK_ENTER, ENTER, false);
		addMapping(VK_ENTER, DROP, true);
		addMapping(VK_SPACE, ENTER, false);
		addMapping(VK_SPACE, DROP, true);
		
		addMapping(VK_U, PUT_4, false);
		addMapping(VK_I, PUT_5, false);
		addMapping(VK_O, PUT_6, false);
		addMapping(VK_J, PUT_1, false);
		addMapping(VK_K, PUT_2, false);
		addMapping(VK_L, PUT_3, false);
		addMapping(VK_N, PUT_0, false);
		addMapping(VK_M, PUT_0, false);
		
		addMapping(VK_H, EXP, false);
		addMapping(VK_Y, LN, false); 
		addMapping(VK_H, EXP10, true);
		addMapping(VK_Y, LOG, true); 
		addMapping(VK_P, PI, false); 
		addMapping(VK_P, DRG, true); 

		addMapping(VK_Z, COMBO, false);
		addMapping(VK_Z, PERM, true);
		addMapping(VK_X, SIN, false);
		addMapping(VK_X, ASIN, true);
		addMapping(VK_C, COS, false);
		addMapping(VK_C, ACOS, true);
		addMapping(VK_V, TAN, false);
		addMapping(VK_V, ATAN, true);
		addMapping(VK_B, PM, false);
		
		addMapping(VK_A, DIV, false); 
		addMapping(VK_S, SUB, false); 
		addMapping(VK_D, MULT, false); 
		addMapping(VK_F, ADD, false); 
		addMapping(VK_G, MACRO_RUN, false); 

		addMapping(VK_Q, SQRT, false); 
		addMapping(VK_Q, ROOT, true); 
		addMapping(VK_W, SWAP, false); 
		addMapping(VK_E, POW, false); 
		addMapping(VK_E, SQUARE, true); 
		addMapping(VK_R, RECIP, false);
		addMapping(VK_T, MACRO_TOGGLE, false); 
		addMapping(VK_T, STORE, true); 
		
		addMapping(VK_DELETE, CLEAR_ENTRY, false);
		addMapping(VK_DELETE, CLEAR_STACK, true);
		addMapping(VK_PAGE_DOWN, CYCLE, false);
		addMapping(VK_W, CYCLE, true);
		addMapping(VK_PAGE_UP, REVERSE_CYCLE, false);
		addMapping(VK_SLASH, RAND, true);
	}

	public static String getShortcut(Command c) {
		KeyStroke stroke = getStroke(c);
		if (stroke == null) {
			return "";
		} else {
			String toRet = KeyEvent.getKeyText(stroke.getKeyCode());
			
			if (REPLACEMENT_MAP.get(toRet) != null) {
				toRet = REPLACEMENT_MAP.get(toRet);
			}

			if ((stroke.getModifiers() & InputEvent.SHIFT_DOWN_MASK) != 0) {
				if (SHIFT_REPLACEMENT_MAP.get(toRet) != null) {
					toRet = SHIFT_REPLACEMENT_MAP.get(toRet);
				}
			} else {
				toRet = toRet.toLowerCase();
			}
			toRet = toRet.replaceAll("numpad-", "");
			toRet = toRet.replaceAll("numpad", "");
			return toRet;
		}
	}
	
	private static KeyStroke getStroke(Command command) {
		if (command == null) {
			return null;
		} else {
			return reverseMap.get(command);
		}
	}
	
	public static Command getCommand(KeyStroke stroke) {
		return shortcutMap.get(stroke);
	}

	public static void addMapping(KeyStroke stroke, Command com) {
		shortcutMap.put(stroke, com);
		reverseMap.put(com, stroke);
	}

	public static void addMapping(int key, Command com) {
		addMapping(key, com, true);
		addMapping(key, com, false);
	}
	
	public static void addMapping(int key, Command com, boolean shiftOn) {
		if (shiftOn) {
			addMapping(KeyStroke.getKeyStroke(key, InputEvent.SHIFT_DOWN_MASK), com);
		} else {
			addMapping(KeyStroke.getKeyStroke(key, 0), com);
		}
	}
}
