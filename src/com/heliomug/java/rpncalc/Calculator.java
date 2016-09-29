package com.heliomug.java.rpncalc;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

public class Calculator {
	public static final int TEXT_INPUT_OFF = 0;
	public static final int TEXT_INPUT_COMMAND = 1;
	public static final int TEXT_INPUT_NAME = 2;
	
	private Display display;
	
	private CalculatorSettings calculatorSettings;
	
	private ArrayList<Stack> stackHistory; 

	private boolean isEntryEditable;
	private boolean isEntryClears;
	
	private String textInputString;

	private int textInputMode;
	private boolean isMacroRecording;
	
	private KeyMap keyMap;
	
	private MacroList macroList;
	private Macro activeMacro;
	private Stack stack;
	
	private Preferences preferences;
	
	public Calculator() {
		setupCalc();

		setupSettings();
		
		setupKeyboardLayout();

		setupDisplay();

		loadPreferencesAndSettings();

		display.repaint();
		display.setVisible(true);
	}

	private void reset() {
		stackHistory.clear();
		isEntryEditable = false;
		isEntryClears = false;
		textInputMode = TEXT_INPUT_OFF; 
	}
	
	// getters
	public Display getDisplay() { return this.display; }
	public KeyMap getKeyMap() { return this.keyMap; }
	public Preferences getPreferences() { return this.preferences; }
	public CalculatorSettings getSettings() { return this.calculatorSettings; }

	public int getTextInputMode() { return this.textInputMode; }
	public String getTextInputString() { return this.textInputString; }
	
	public Stack getStack() { return this.stack; }
	public ArrayList<Stack> getStackHistory() { return this.stackHistory; }
	public MacroList getMacroList() { return this.macroList; }
	public Macro getActiveMacro() { return this.activeMacro; }

	public boolean isMacroRecording() { return this.isMacroRecording; } 
	public boolean isEntryEditable() { return this.isEntryEditable; }
	public boolean isEntryClears() { return this.isEntryClears; }
	
	
	// setters
	public void setStack(Stack ms) { this.stack = ms; }

	public void setTextInputMode(int tim) { this.textInputMode = tim; } 
	public void resetTextInputString() { textInputString = ""; getDisplay().repaint(); }

	public void setActiveMacro(Macro m) { this.activeMacro = m; }
	public void setMacroRecording(boolean b) { this.isMacroRecording = b; }
	public void resetMacro() { this.activeMacro = new Macro(); }

	public void setEntryEditable(boolean b) { this.isEntryEditable = b; }
	public void setEntryClears(boolean b) { this.isEntryClears = b; }

	public void resetStack() { stack = new Stack(); }
	public void resetSettings() { calculatorSettings = new CalculatorSettings(); }
	public void resetMacros() { 
		macroList = new MacroList(); 		
		activeMacro = null;
	}
	
	// constructor stuff
	private void setupSettings() { calculatorSettings = new CalculatorSettings(); }
	
	private void setupCalc() {
		if (stack == null) stack = new Stack();
		stackHistory = new ArrayList<Stack>();
		if (macroList == null) macroList = new MacroList();
		isEntryEditable = false;
		isEntryClears  = false;
		textInputString = "";
		textInputMode = TEXT_INPUT_OFF;
	}
	
	private void setupKeyboardLayout() { keyMap = KeyMap.getStandardKeyMap();	}
	
	private void setupDisplay() {
		display = new Display(this);
	}
	
	private void loadPreferencesAndSettings() {
		preferences = Preferences.load();
		if (preferences == null) preferences = new Preferences();


		if (preferences.getContinueFromExit()) {
			if (SavablePackage.loadDefault() != null) {
				setAllFromSaveStuff(SavablePackage.loadDefault());
			} else {
				getDisplay().showErrorPopup("Previous calculator could not be loaded.  Starting calculator with default settings.  ");
				calculatorSettings = new CalculatorSettings();
			}
		} else {
			calculatorSettings = new CalculatorSettings();
		}
	}

	
	// runner
	protected boolean executeCommand(Command command) {
		display.setMessage(""); 

		boolean isMacroRunningBefore = isMacroRecording; 
		boolean successfulRun = command.run(this);
		boolean isMacroRunningAfter = isMacroRecording; 

		if (isMacroRunningBefore && isMacroRunningAfter && successfulRun && command.isRecordable()) {
			activeMacro.addCommand(command);
			display.setMacroDisplayAddCommand(command); 
		}
		
		if (successfulRun)	{
			stackHistory.add(0, stack.copy());
		} else {
			display.notifyFailure();
		}

		display.repaint();

		return successfulRun;
	}

	
	// handle keys in general
	public void handleKey(KeyEvent e) {
		if (e.isAltDown() || e.isControlDown()) {
			return;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			StandardCommand.TEXT_INPUT_CANCEL.run(this);
		}
		
		if (textInputMode == TEXT_INPUT_OFF) {
			if (e.getKeyCode() == KeyEvent.VK_SLASH && e.isShiftDown() == false) {
				if (!StandardCommand.COMMAND_INPUT_START.run(this)) getDisplay().notifyFailure();
			} else{
				handleKeyNormal(e);
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_SLASH && e.isShiftDown() == false) {
				StandardCommand.TEXT_INPUT_CANCEL.run(this);
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				StandardCommand.TEXT_INPUT_FINISH.run(this);
			} else {
				textInputHandleKey(e);
			}
		}
		
	}

	// handle quick commands
	private void handleKeyNormal(KeyEvent e) {
		display.setTextInputMessage("");
		Command command = keyMap.getCommand(e);
		if (command != null) executeCommand(command);
	}
	
	// text input
	private void textInputHandleKey(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (textInputString.length() > 0) {
				textInputString = textInputString.substring(0, textInputString.length() - 1);
				display.setTextInputLabel(textInputString);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
			fillCommandStringFromPartialString(textInputString);
		} else if (e.getKeyCode() != KeyEvent.VK_SLASH && e.getKeyCode() != KeyEvent.VK_SHIFT) {
			textInputString += e.getKeyChar();
			display.setTextInputLabel(textInputString);
		}
	}

	
	// for tab completion
	protected ArrayList<Command> getNamedCommands() {
		ArrayList<Command> toRet = new ArrayList<Command>();
		for (Command command : StandardCommand.values()) {
			if (command.getName() != null) {
				toRet.add(command);
			}
		}
		for (Command command : macroList) {
			if (command.getName() != null) {
				toRet.add(command);
			}
		}
		return toRet;
	}
	
	protected void fillCommandStringFromPartialString(String partialName) {
		partialName = partialName.toLowerCase();
		int partialLength = partialName.length();

		ArrayList<String> possibleNames = new ArrayList<String>();
		for (Command named : getNamedCommands()) {
			String name = named.getName();
			if (name != null && name.length() >= partialLength && name.substring(0, partialLength).toLowerCase().equals(partialName)) {
				possibleNames.add(name);
			}
		}

		Collections.sort(possibleNames);
		
		if (possibleNames.size() == 1) {
			textInputString = possibleNames.get(0);
			display.setTextInputLabel(textInputString);
		} else if (possibleNames.size() == 0) {
			getDisplay().showNoPossibleCommandError(partialName);  
			resetTextInputString();
			display.setTextInputLabel(textInputString);
		} else {
			String toDisplay = "Multiple commands start with \"" + partialName + "\": \n";
			for (String name : possibleNames) {
				toDisplay += " - "+ name + "\n";
			}
			getDisplay().showClarificationMessage(toDisplay);
		}
	}
	
	
	// save, load, quit
	public void quit() {
		preferences.save();
		if (preferences.getContinueFromExit()) {
			new SavablePackage(calculatorSettings, getDisplay().getDisplaySettings(), stack, macroList, activeMacro).saveDefault();
		}
		System.exit(0);
	}

	public boolean save() {
		boolean success = new SavablePackage(calculatorSettings, getDisplay().getDisplaySettings(), stack, macroList, activeMacro).save();
		if (success) getDisplay().setMessage(Display.CALCULATOR_SAVED);
		return success;
	}
	
	
	public void setAllToDefault() {
		setupCalc();
		resetSettings();
		resetMacros();
		resetStack();
		getDisplay().setDisplaySettings(new DisplaySettings());
	}
	
	private void setAllFromSaveStuff(SavablePackage ss) {
		calculatorSettings = ss.getCalculatorSettings();
		getDisplay().setDisplaySettings(ss.getFrameSettings());
		stack = ss.getStack();
		activeMacro = ss.getActiveMacro();
		macroList = ss.getMacroList();
	}
	
	protected void load() {
		SavablePackage loadedStuff = SavablePackage.load();
		if (loadedStuff != null) {
			reset();
			setAllFromSaveStuff(loadedStuff);
			display.setMessage(Display.CALCULATOR_LOADED);
			display.repaint();
		}
	}

}
