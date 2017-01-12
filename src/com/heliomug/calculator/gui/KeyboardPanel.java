package com.heliomug.calculator.gui;

import static com.heliomug.calculator.StandardCommand.ACOS;
import static com.heliomug.calculator.StandardCommand.ADD;
import static com.heliomug.calculator.StandardCommand.ASIN;
import static com.heliomug.calculator.StandardCommand.ATAN;
import static com.heliomug.calculator.StandardCommand.BACKSPACE;
import static com.heliomug.calculator.StandardCommand.CLEAR_ENTRY;
import static com.heliomug.calculator.StandardCommand.CLEAR_STACK;
import static com.heliomug.calculator.StandardCommand.COMBO;
import static com.heliomug.calculator.StandardCommand.COS;
import static com.heliomug.calculator.StandardCommand.CYCLE;
import static com.heliomug.calculator.StandardCommand.DIV;
import static com.heliomug.calculator.StandardCommand.DRG;
import static com.heliomug.calculator.StandardCommand.DROP;
import static com.heliomug.calculator.StandardCommand.ENTER;
import static com.heliomug.calculator.StandardCommand.EXIT;
import static com.heliomug.calculator.StandardCommand.EXP;
import static com.heliomug.calculator.StandardCommand.EXP10;
import static com.heliomug.calculator.StandardCommand.FACT;
import static com.heliomug.calculator.StandardCommand.LN;
import static com.heliomug.calculator.StandardCommand.LOG;
import static com.heliomug.calculator.StandardCommand.MACRO_RUN;
import static com.heliomug.calculator.StandardCommand.MACRO_TOGGLE;
import static com.heliomug.calculator.StandardCommand.MOD;
import static com.heliomug.calculator.StandardCommand.MULT;
import static com.heliomug.calculator.StandardCommand.PERM;
import static com.heliomug.calculator.StandardCommand.PI;
import static com.heliomug.calculator.StandardCommand.PM;
import static com.heliomug.calculator.StandardCommand.POW;
import static com.heliomug.calculator.StandardCommand.PUT_0;
import static com.heliomug.calculator.StandardCommand.PUT_1;
import static com.heliomug.calculator.StandardCommand.PUT_2;
import static com.heliomug.calculator.StandardCommand.PUT_3;
import static com.heliomug.calculator.StandardCommand.PUT_4;
import static com.heliomug.calculator.StandardCommand.PUT_5;
import static com.heliomug.calculator.StandardCommand.PUT_6;
import static com.heliomug.calculator.StandardCommand.PUT_7;
import static com.heliomug.calculator.StandardCommand.PUT_8;
import static com.heliomug.calculator.StandardCommand.PUT_9;
import static com.heliomug.calculator.StandardCommand.PUT_DECIMAL;
import static com.heliomug.calculator.StandardCommand.RAND;
import static com.heliomug.calculator.StandardCommand.RECIP;
import static com.heliomug.calculator.StandardCommand.ROOT;
import static com.heliomug.calculator.StandardCommand.SIN;
import static com.heliomug.calculator.StandardCommand.SQRT;
import static com.heliomug.calculator.StandardCommand.SQUARE;
import static com.heliomug.calculator.StandardCommand.STORE;
import static com.heliomug.calculator.StandardCommand.SUB;
import static com.heliomug.calculator.StandardCommand.SWAP;
import static com.heliomug.calculator.StandardCommand.TAN;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.heliomug.calculator.Command;

@SuppressWarnings("serial")
public class KeyboardPanel extends JPanel {
	private static final Color BORDER_COLOR = Color.BLACK;
	
	private static final Map<Command, String> textMap = new HashMap<>();
	{
		textMap.put(ADD, "+");
		textMap.put(MULT, "\u00d7");
		textMap.put(SUB, "-");
		textMap.put(DIV, "\u00f7");
		textMap.put(MOD, "%");
		
		textMap.put(PM, "+/-");
		textMap.put(RECIP, "1/x");
		
		textMap.put(FACT, "!");

		textMap.put(POW, "y^x");
		textMap.put(SQUARE, "x\u00b2");
		textMap.put(SQRT, "\u221a");
		textMap.put(ROOT, "x\u221ay");
		
		textMap.put(SIN, "sin");
		textMap.put(COS, "cos");
		textMap.put(TAN, "tan");
		textMap.put(ASIN, "asin");
		textMap.put(ACOS, "acos");
		textMap.put(ATAN, "atan");

		textMap.put(COMBO, "yCx");
		textMap.put(PERM, "yPx");

		textMap.put(RAND, "rand");

		textMap.put(EXP, "e^x");
		textMap.put(LN, "ln");
		textMap.put(EXP10, "10^x");
		textMap.put(LOG, "log");

		textMap.put(PUT_0, "0");
		textMap.put(PUT_1, "1");
		textMap.put(PUT_2, "2");
		textMap.put(PUT_3, "3");
		textMap.put(PUT_4, "4");
		textMap.put(PUT_5, "5");
		textMap.put(PUT_6, "6");
		textMap.put(PUT_7, "7");
		textMap.put(PUT_8, "8");
		textMap.put(PUT_9, "9");
		textMap.put(PUT_DECIMAL, ".");
		textMap.put(BACKSPACE, "\u2190");
		
		textMap.put(ENTER, "\u25b2");
		textMap.put(DROP, "\u25bc");
		textMap.put(SWAP, "\u2194");
		textMap.put(CYCLE, "\u21ba");

		textMap.put(STORE, "sto");
		textMap.put(PI, "\u03c0");

		textMap.put(CLEAR_ENTRY, "ce");
		textMap.put(CLEAR_STACK, "cls");
		
		textMap.put(MACRO_TOGGLE, "rec/stop");
		textMap.put(MACRO_RUN, "run");

		textMap.put(DRG, "DRG");
		textMap.put(EXIT, "X");
		//textMap.put(, "\u2600\u2615");
	}
	
	private static final Command[][] LAYOUT = new Command[][] {
		{null, SWAP, STORE, CLEAR_ENTRY, PUT_7, PUT_8, PUT_9, DIV},
		{LN, EXP, SQRT, POW, PUT_4, PUT_5, PUT_6, MULT},
		{SIN, COS, TAN, RECIP, PUT_1, PUT_2, PUT_3, SUB},
		{MOD, COMBO, PI, PM, PUT_0, PUT_DECIMAL, BACKSPACE, ADD},
		{MACRO_TOGGLE, MACRO_RUN, DROP, ENTER}
	};
	
	private static final Command[][] ALT = new Command[][] {
		{null, CYCLE, RAND, CLEAR_STACK, PUT_7, PUT_8, PUT_9, DIV},
		{LOG, EXP10, ROOT, SQUARE, PUT_4, PUT_5, PUT_6, MULT},
		{ASIN, ACOS, ATAN, null, PUT_1, PUT_2, PUT_3, SUB},
		{FACT, PERM, DRG, null, PUT_0, PUT_DECIMAL, BACKSPACE, ADD},
	};
	
	private String getText(Command command) {
		if (command == null) {
			return "";
		}
		
		if (textMap.containsKey(command)) {
			return textMap.get(command);
		} else {
			return command.getAbbrev();
		}
	}
	
	public KeyboardPanel() {
		super();
		setLayout(new GridLayout(0, 1));
		setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		
		for (int i = 0 ; i < LAYOUT.length ; i++) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, LAYOUT[i].length));
			for (int j = 0 ; j < LAYOUT[i].length ; j++) {
				Command command = LAYOUT[i][j];
				String text = getText(command);
				Command alt = null;
				String  altText = "";
				if (ALT.length > i && ALT[i].length > j) {
					alt = ALT[i][j];
					altText = getText(alt);
				}
				if (command == alt) { 
					panel.add(new Button(command, text));
				} else {
					panel.add(new Button(command, text, alt, altText));
				}
			}
			add(panel);
		}
		
	}
}
