package com.heliomug.java.rpncalc;

import java.io.Serializable;

public enum StandardCommand implements Command, Serializable {
	// number entry stuff
	INSERT_0("< 0 >", "0", true, new InsertDigit(0)),  
	INSERT_1("< 1 >", "1", true, new InsertDigit(1)),  
	INSERT_2("< 2 >", "2", true, new InsertDigit(2)),  
	INSERT_3("< 3 >", "3", true, new InsertDigit(3)),  
	INSERT_4("< 4 >", "4", true, new InsertDigit(4)),  
	INSERT_5("< 5 >", "5", true, new InsertDigit(5)),  
	INSERT_6("< 6 >", "6", true, new InsertDigit(6)),  
	INSERT_7("< 7 >", "7", true, new InsertDigit(7)),  
	INSERT_8("< 8 >", "8", true, new InsertDigit(8)), 
	INSERT_9("< 9 >", "9", true, new InsertDigit(9)),  
	ADD_DECIMAL("< . >", ".", true, new CommandStump() {
		@Override public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.isEntryClears() && m.size() > 0) m.pop();
			if (c.isEntryClears() || !c.isEntryEditable()) m.push(new Num());
			m.push(m.pop().addDecimalPoint());
			c.setEntryEditable(true);
			c.setEntryClears(false);
			return true;
		}		
	}),
	BACKSPACE("< \u2190 >", "backspace", "\u2190", true, new CommandStump() {
		@Override public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (m.size() < 1) {
				return false;
			} else if (!c.isEntryEditable()) {
				return StandardCommand.CLEAR_ENTRY.run(c);
			} else {
				c.setEntryEditable(true);
				c.setEntryClears(false);
				m.push(m.pop().delDigit());
				return true;
			}
		}
	}), 
	NEG("<+/->", "pm", "+/-", true, new CommandStump() {
		@Override public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (m.isEmpty()) {
				return false;
			} else {
				m.push(m.pop().negify());
				c.setEntryClears(false);
				return true;
			}
		}
	}),

	// four function
	ADD("<add>", "add", "+", true, new CommandStandardReqs(2, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(m.pop().add(m.pop()));
			return true;
		}
	})),
	MULTIPLY("<mul>", "multiply", "\u00d7", true, new CommandStandardReqs(2, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(m.pop().multiply(m.pop()));
			return true;
		}
	})), 
	SUBTRACT("<sub>", "subtract", "\u2212", true, new CommandStandardReqs(2, new CommandStump() { public boolean run(Calculator c) {
		Stack m = c.getStack();
		Num a = m.pop();
		Num b = m.pop();
		m.push(b.subtract(a));
		return true;
	}})), 
	DIVIDE("<div>", "divide", "\u00f7", true, new CommandStandardReqs(2, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			Num a = m.pop();
			if (a.isZero()) return false;
			Num b = m.pop();
			m.push(b.divide(a));
			return true;
		}
	})), 

	// integer ops
	MOD("<mod>", "mod", "mod", true, new CommandStump() {
		@Override public boolean run(Calculator c) {
			Stack ms = c.getStack();
			if (ms.size() >= 2 && ms.deepPeek(0).isInteger() && ms.deepPeek(1).isInteger()) {
				Num n1 = ms.pop();
				Num n2 = ms.pop();
				ms.push(new Num(n2.integerValue() % n1.integerValue()));
				return true;
			} else {
				return false;
			}
		}
	}),
	COMBO("<nCr>", "combination", "nCr", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (passStandardReqs(c, 2) && c.getStack().peek().isInteger() && c.getStack().deepPeek(1).isInteger()) {
				int a = c.getStack().pop().integerValue();
				int b = c.getStack().pop().integerValue();
				int prod = 1;
				for (int i = b ; i > b - a ; i--) {
					prod *= i;
				}
				for (int i = 2 ; i <= a ; i++) {
					prod /= i;
				}
				c.getStack().push(new Num(prod));
				return true;
			} else {
				return false;
			}
		}
	}),
	PERM("<nPr>", "permutation", "nPr", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (passStandardReqs(c, 2) && c.getStack().peek().isInteger() && c.getStack().deepPeek(1).isInteger()) {
				int a = c.getStack().pop().integerValue();
				int b = c.getStack().pop().integerValue();
				int prod = 1;
				for (int i = b ; i > b - a ; i--) {
					prod *= i;
				}
				c.getStack().push(new Num(prod));
				return true;
			} else {
				return false;
			}
		}
	}),
	FACT("< ! >", "factorial", "!", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (passStandardReqs(c, 1) && c.getStack().peek().isInteger()) {
				int prod = 1;
				int lim = c.getStack().pop().integerValue();
				for (int i = 2 ; i <= lim ; i++) {
					prod *= i;
				}
				c.getStack().push(new Num(prod));
				return true;
			} else {
				return false;
			}
		}
	}),

	// powers
	SQRT("<srt>", "sqrt", "\u221a", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			if (passStandardReqs(c, 1) && !c.getStack().peek().isNegative()) {
				Stack m = c.getStack();
				m.push(m.pop().sqrt());
				return true;
			} else {
				return false;
			}
		}
	})),
	ROOT("<rt >", "root", "root", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			Num exp = m.pop().reciprocal();
			Num base = m.pop();
			m.push(base.pow(exp));
			return true;
		}
	})),
	SQUARE("<x^2>", "square", "x\u00b2", true, new CommandStandardReqs(1, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(m.pop().pow(new Num(2)));
			return true;
		}		
	})),  
	POW("<y^x>", "power", "y^x", true, new CommandStandardReqs(2, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			Num exp = m.pop();
			Num base = m.pop();
			m.push(base.pow(exp));
			return true;
		}
	})),  
	FLIP("<1/x>", "reciprocal", "1/x", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (passStandardReqs(c, 1) && !c.getStack().peek().isZero()) {
				Stack m = c.getStack();
				m.push(m.pop().reciprocal());
				return true;
			} else {
				return false;
			}
		}
	}),

	// exp
	LN("<ln>", "ln", "ln", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(m.pop().ln());
			return true;
		}
	})),
	LOG_10("<log>", "logarithm", "log", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(new Num(Math.log10(m.pop().doubleValue())));
			return true;
		}
	})),
	EXP("<exp>", "exp", "e^x", true, new CommandStandardReqs(1, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(m.pop().exp());
			return true;
		}
	})),
	MAGNITUDE("<10^>", "10^x", "10^x", true, new CommandStandardReqs(1, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.push(new Num(Math.pow(10, m.pop().doubleValue())));
			return true;
		}
	})),

	// trig
	SIN("<sin>", "sin", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.RADIAN) {
				m.push(m.pop().sin());
			} else if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.DEGREE) {
				m.push(m.pop().degToRad().sin());
			}
			return true;
		}
	})),
	COS("<cos>", "cos", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.RADIAN) {
				m.push(m.pop().cos());
			} else if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.DEGREE) {
				m.push(m.pop().degToRad().cos());
			}
			return true;
		}
	})),
	TAN("<tan>", "tan", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.RADIAN) {
				m.push(m.pop().tan());
			} else if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.DEGREE) {
				if (m.peek().degToRad().subtract(new Num(90)).divide(new Num(180)).isInteger()) {
					return false;
				}
				m.push(m.pop().degToRad().tan());
			}
			return true;
		}
	})),
	ASIN("<asn>", "asin", "asin", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.RADIAN) {
				m.push(m.pop().asin());
			} else if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.DEGREE) {
				m.push(m.pop().asin().radToDeg());
			}
			return true;
		}
	})), 
	ACOS("<acs>", "acos", "acos", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.RADIAN) {
				m.push(m.pop().acos());
			} else if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.DEGREE) {
				m.push(m.pop().acos().radToDeg());
			}
			return true;
		}
	})),
	ATAN("<atn>", "atan", "atan", true, new CommandStandardReqs(1, new CommandStump() { 
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.RADIAN) {
				m.push(m.pop().atan());
			} else if (c.getSettings().getAngleMode() == CalculatorSettings.AngleMode.DEGREE) {
				m.push(m.pop().atan().radToDeg());
			}
			return true;
		}
	})),

	// stack stuff
	SWAP("<swp>", "swap", "\u2194", true, new CommandStandardReqs(2, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			Num a = m.pop();
			Num b = m.pop();
			m.push(a);
			m.push(b);
			return true;
		}
	})),
	CYCLE("<cyc>", "cycle", "\u21ba", true, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.cycle();
			c.setEntryEditable(false);
			return true;
		}
	}), 
	REVERSE_CYCLE("<rcy>", "reverse cycle", "\u21bb", true, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.reverseCycle();
			c.setEntryEditable(false);
			return true;
		}
	}), 
	ENTER("<ent>", "Enter", "\u25b2", true, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (m.size() < 1) {
				m.push(new Num());
				m.push(new Num());
				c.setEntryClears(true);
				c.setEntryEditable(false);
			} else {
				m.push(m.peek().copy());
				c.setEntryClears(true);
				c.setEntryEditable(false);
			}
			return true;
		}
	}),  
	DROP("<drp>", "Drop", "\u25bC", true, new CommandStandardReqs(1, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			m.pop();
			return true;
		}
	})), 
	CLEAR_ENTRY("<clr>", "clear", "ce", true, new CommandStandardReqs(1, new CommandStump() {
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (m.isEmpty()) {
				return false;
			} else {
				m.pop();
				m.push(new Num());
				c.setEntryClears(false);
				c.setEntryEditable(true);
				return true;
			}
		}
	})), 
	CLEAR_STACK("<cls>", "cls", "cls", true, new CommandStump() {
		public boolean run(Calculator c) {
			c.setStack(new Stack());
			c.setEntryClears(false);
			c.setEntryEditable(false);
			return true;
		}
	}), 

	// constants
	PI("<pi>", "pi", "\u03c0", true, new EnterDouble(Math.PI)),
	E("< e >", "e", true, new EnterDouble(Math.E)),

	// misc
	RAND("<ran>", "random", "rand", true, new CommandStump() {
		public boolean run(Calculator c) {
			c.getStack().push(new Num(Math.random()));
			c.setEntryEditable(false);
			return true;
		}	
	}), 
	ROUND("<rou>", "round", "rou", true, new CommandStump() {
		@Override public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (m.isEmpty()) {
				return false;
			} else {
				m.push(new Num(Math.round(m.pop().doubleValue())));
				c.setEntryClears(false);
				c.setEntryEditable(false);
				return true;
			}
		}
	}),

	// storage
	STORE("<sto>", "store", "sto", true, new CommandStandardReqs(1, new CommandStump() {
		public boolean run(Calculator c) {
			Macro newMacro = new Macro(c.getStack().peek().copy());
			c.setActiveMacro(newMacro);
			StandardCommand.MACRO_NAME_START.run(c);
			c.setEntryEditable(false);
			return true;
		}		
	})), 
	
	// macro stuff
	MACRO_START_STOP("<s/s>", "start/stop recording", "s/s", true, new CommandStump() {
		public boolean run(Calculator c) {
			return (StandardCommand.MACRO_START.run(c) || StandardCommand.MACRO_STOP.run(c));
		}
	}),
	MACRO_START("<rec>", "record", "rec", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getTextInputMode() == Calculator.TEXT_INPUT_OFF && !c.isMacroRecording()) {
				c.getDisplay().setMacroText(Display.MACRO_ON_TEXT);
				c.setMacroRecording(true);
				c.resetMacro();
				return true;
			}
			return false;
		}
	}),
	MACRO_STOP("<stp>", "stop", "stp", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.isMacroRecording()) {
				c.getDisplay().setMacroDisplayNoRecord();
				c.setMacroRecording(false);
				return true;
			}
			return false;
		}
	}),
	MACRO_RUN("<run>", "run", "run", true, new CommandStump() {
		public boolean run(Calculator c) {
			Stack oldStack = c.getStack().copy();
			boolean success = true;
			if (c.getActiveMacro() == null || c.isMacroRecording()) {
				success = false;
			} else {
				success = c.getActiveMacro().run(c);
			}
			if (!success){
				c.setStack(oldStack);
			}
			return success;
		}
	}), 
	MACRO_DELETE("<del>", "remove macro", "del", true, new CommandStump() {
		public boolean run(Calculator c) {
			Macro active = c.getActiveMacro();
			if (c.isMacroRecording() || active == null) {
				return false; 
			} else {
				c.getDisplay().setMessage(String.format(Display.MACRO_REMOVED, active.getAbbreviation()));
				c.setActiveMacro(null);
				c.getDisplay().repaint();
				return c.getMacroList().removeMacroFromAvailable(active);
			}
		}
	}), 

	// text input
	COMMAND_INPUT_START("<com>", "command", false, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getTextInputMode() != Calculator.TEXT_INPUT_OFF) {
				return false;
			} else {
				c.getDisplay().setTextInputMessage(Display.TEXT_INPUT_READY); 
				c.resetTextInputString();
				c.setTextInputMode(Calculator.TEXT_INPUT_COMMAND);
				return true;
			}
		}
	}),
	COMMAND_INPUT_FINISH(null, null, false, new CommandStump() {
		public boolean run(Calculator c) {
			String maybeName = c.getTextInputString();
			Command command = StandardCommand.getCommand(maybeName);

			if (command == null) {
				Macro macro = c.getMacroList().getMacro(maybeName);
				if (macro != null) {
					if (!c.isMacroRecording()) {
						c.setActiveMacro(macro);
						c.getDisplay().setMacroDisplayNoRecord();
					}
					command = macro;
				}
			}

			c.getDisplay().setTextInputLabel("");
			c.getDisplay().setTextInputMessage("");
			c.resetTextInputString();
			c.setTextInputMode(Calculator.TEXT_INPUT_OFF);
			c.getDisplay().repaint();
			
			if (command == null) {
				c.getDisplay().setTextInputMessage(Display.TEXT_INPUT_NOT_FOUND + maybeName); 
				return false;
			} else {
				c.getDisplay().setMessage("");
				c.executeCommand(command);
				return true;
			}
		}
	}),
	MACRO_NAME_START("<nom>", "name", "name", false, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getActiveMacro() == null || c.isMacroRecording() || c.getTextInputMode() != Calculator.TEXT_INPUT_OFF) {
				return false;
			} else { 
				c.getDisplay().setTextInputMessage(Display.MACRO_NAME_INPUT_READY_TEXT);
				c.resetTextInputString();
				c.setTextInputMode(Calculator.TEXT_INPUT_NAME);
				return true;
			}
		}
	}),
	MACRO_NAME_FINISH(null, null, false, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getActiveMacro() == null || c.isMacroRecording()) {
				return false;
			} else if (StandardCommand.getCommand(c.getTextInputString()) != null) {
				c.getDisplay().setMessage("Name taken. Pick another.");
				c.resetTextInputString();
				c.setTextInputMode(Calculator.TEXT_INPUT_NAME);
				return false;
			} else {
				c.getActiveMacro().setName(c.getTextInputString());
				c.getMacroList().addMacroToAvailable(c.getActiveMacro());
				c.getDisplay().setMacroDisplayNoRecord();
				return true;
			}
		}
	}),
	TEXT_INPUT_CANCEL("<esc>", "escape", "esc", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.getDisplay().setTextInputLabel("");
			c.getDisplay().setTextInputMessage(Display.TEXT_INPUT_CANCELLED);
			c.resetTextInputString();
			c.setTextInputMode(Calculator.TEXT_INPUT_OFF);
			c.setActiveMacro(null);
			c.getDisplay().repaint();
			return true;
		}
	}),
	TEXT_INPUT_FINISH(null, null, false, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getTextInputMode() == Calculator.TEXT_INPUT_COMMAND) {
				return StandardCommand.COMMAND_INPUT_FINISH.run(c);
			} else if (c.getTextInputMode() == Calculator.TEXT_INPUT_NAME) {
				if (StandardCommand.MACRO_NAME_FINISH.run(c)) {
					c.getDisplay().setMessage("");
					c.getDisplay().setTextInputLabel("");
					c.getDisplay().setTextInputMessage("");
					c.setTextInputMode(Calculator.TEXT_INPUT_OFF);
					c.resetTextInputString();
					return true;
				} 
			}
			return false;
		}
	}),
	
	// calc settings
	TOGGLE_ANGLE_MODE("<drg>", "drg", "drg", true, new CommandStump() {
		public boolean run(Calculator c) {
			c.getSettings().toggleAngleMode();
			return true;
		}
	}),
	
	// undo
	UNDO(" \u21b6 ", "undo", "Undo \u21b6", true, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getStackHistory().size() == 0 || c.isMacroRecording()) {
				return false;
			} else if (c.getStackHistory().size() == 1) {
				c.getStackHistory().clear();
				c.setEntryEditable(false);
				c.setStack(new Stack());
				return true;
			} else {
				// remove the undo command
				c.getStackHistory().remove(0);
				//c.stackForwardHistory().add(c.mathStack().copy());
				Stack oldStack = c.getStackHistory().remove(0).copy();
				c.setStack(oldStack);
				return true;
			}
		}
	}),

	// save / load
	RESET_ALL(null, "resetAll", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.setAllToDefault();
			return true;
		}
	}),
	SAVE_ALL(null, "save", false, new CommandStump() {
		public boolean run(Calculator c) {
			return c.save();
		}
	}),
	LOAD_ALL(null, "load", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.load();
			return true;
		}
	}),
	RESET_MACROS(null, "clearMacros", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.resetMacros();
			c.getDisplay().repaint();
			return true;
		}
	}),
	SAVE_MACROS(null, "saveMacros", false,  new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getMacroList().save()) {
				c.getDisplay().setMessage(Display.MACROS_SAVED); 
				return true;
			} 
			return false;
		}
	}),
	LOAD_MACROS(null, "loadMacros", false, new CommandStump() {
		public boolean run(Calculator c) {
			if (c.getMacroList().load()) {
				c.getDisplay().setMessage(Display.MACROS_LOADED);
				return true;
			}
			return false;
		}
	}),
	RESET_SETTINGS(null, "clearSettings", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.resetSettings();
			c.getDisplay().repaint();
			return true;
		}
	}),
	SAVE_SETTINGS(null, "saveSettings", false, new CommandStump(){
		public boolean run(Calculator c) {
			c.getPreferences().save();
			c.getDisplay().setMessage(Display.CONFIG_SAVED); 
			return true;
		}
	}),
	LOAD_SETTINGS(null, "loadSettings", false, new CommandStump() {
		public boolean run(Calculator c) {
			DisplaySettings loadedConfig = DisplaySettings.load();
			if (loadedConfig != null) {
				c.getDisplay().setDisplaySettings(loadedConfig);
				c.getDisplay().setMessage(Display.CONFIG_LOADED); 
				return true;
			}
			return false;
		}
	}),
	SAVE_STACK(null, "saveStack", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.getStack().save();
			c.getDisplay().setMessage(Display.STACK_SAVED);
			return true;
		}
	}),
	LOAD_STACK(null, "loadStack", false, new CommandStump() {
		public boolean run(Calculator c) {
			Stack ms = Stack.load();
			if (ms != null) {
				c.setStack(ms);
				c.getDisplay().setMessage(Display.STACK_LOADED); 
				return true;
			}
			return false;
		}
	}),

	// quit or exit
	EXIT("<exit>", "exit", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.quit();
			return true;
		}
	}), 
	QUIT("<exit>", "quit", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.quit();
			return true;
		}
	}), 
	Q("<exit>", "q", false, new CommandStump() {
		public boolean run(Calculator c) {
			c.quit();
			return true;
		}
	}), 

	VOID("<void>", "", "", false, new CommandStump() {
		public boolean run(Calculator c) {
			return false;
		}
	});
	
	// TO DO: 
	// rebase
	// show
	// if
	// loop
	
	private final CommandStump commandStump;
	private final String abbreviation;
	private final String commandString;
	private final String keyboardString;
	private final boolean isRecordable;
	
	// constructor
	StandardCommand(String str, String commandString, boolean isRecordable, CommandStump command) {
		this(str, commandString, commandString, isRecordable, command);
	}
	StandardCommand(String str, String commandString, String keyboardString, boolean isRecordable, CommandStump command) {
		this.commandStump = command;
		this.abbreviation = str;
		this.commandString = commandString;
		this.keyboardString = keyboardString;
		this.isRecordable = isRecordable;
	}

	
	// constructor helpers
	private interface CommandStump {
		boolean run(Calculator c);
	}
	
	private static class CommandStandardReqs implements CommandStump {
		int minStackSize;
		CommandStump cmd;
		
		public CommandStandardReqs(int minStackSize, CommandStump cmd) {
			this.minStackSize = minStackSize;
			this.cmd = cmd;
		}
		
		public boolean run(Calculator calc) {
			if (passStandardReqs(calc, minStackSize)) {
				return cmd.run(calc);
			} else {
				return false;
			}
		}
	}
	
	private static boolean passStandardReqs(Calculator c, int minStackSize) {
		if (c.getStack().size() < minStackSize) {
			return false; 
		} else {
			c.setEntryClears(false);
			c.setEntryEditable(false);
			return true;
		}
	}

	
	// getters
	public String getCommandString() { return commandString; }
	public String getName() { return commandString;	}
	public String getAbbreviation() { return abbreviation; }
	public String getButtonString() { return keyboardString; }
	public String toString() { return getAbbreviation(); }
	public boolean isRecordable() { return isRecordable; }
	public static StandardCommand getCommand(String commandString) {
		for (StandardCommand cc : StandardCommand.values()) {
			if (cc.commandString != null && cc.commandString.equals(commandString)) return cc;
		}
		return null;
	}
	
	// runner
	public boolean run(Calculator c) {
		return this.commandStump.run(c);
	}

	// reusable commands
	private static class InsertDigit implements CommandStump {
		private int n;
		
		public InsertDigit(int n) { this.n = n; }
		
		public boolean run(Calculator c) {
			Stack m = c.getStack();
			if (c.isEntryClears() && m.size() > 0) m.pop();
			if (c.isEntryClears() || !c.isEntryEditable()) m.push(new Num());
			m.push(m.pop().addDigit(n));
			c.setEntryEditable(true);
			c.setEntryClears(false);
			return true;
		}
	}
	
	private static class EnterDouble implements CommandStump {
		private double d;
		
		public EnterDouble(double d) { this.d = d; }
		
		public boolean run(Calculator c) {
			if (c.getStack().size() > 0 && c.isEntryClears()) c.getStack().pop();
			c.getStack().push(new Num(d));
			c.setEntryEditable(false);
			return true;
		}
	}

}