package com.heliomug.calculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Calculator implements Function<Command, Boolean>, Serializable {
	private static final long serialVersionUID = 3507252594114632417L;

	private static final String ERROR_MESSAGE = "  !  ";
	
	private static final AngleMode DEFAULT_ANGLE_MODE = AngleMode.DEGREES;
	
	private Stack stack;
	transient private boolean isEntryEditable;
	transient private boolean isEntryClears;
	
	private AngleMode angleMode;
	
	transient private boolean isRecording;
	private Macro currentMacro;
	private List<Macro> macroList;
	
	transient private List<Command> commandList;
	
	transient private String status;
	
	public Calculator() {
		stack = new Stack();
		macroList = new ArrayList<>();
		commandList = new ArrayList<>();
		currentMacro = null;
		isEntryEditable = false;
		isEntryClears = true;
		isRecording = false;
		angleMode = DEFAULT_ANGLE_MODE;
		status = "";
	}
	
	public Stack getStack() { return stack; }
	public boolean isEntryClears() { return isEntryClears; }
	public boolean isEntryEditable() { return isEntryEditable; }
	public boolean isRecording() { return isRecording; }
	public boolean hasCurrentMacro() { return currentMacro != null; }
	public String getStatus() { return status == null ? "" : status; }
	public AngleMode getAngleMode() { return angleMode; }
	public String getCurrentMacroName() { return currentMacro == null ? "[none]" : currentMacro.getName(); } 
	public Macro getMacro(String name) {
		// would be more efficient with a map, 
		// but then have to deal with renaming behind map's back
		for (Macro macro : macroList) {
			if (macro.getName().equals(name)) {
				return macro;
			}
		}
		return null;
	}
	public void setMacro(String name) {
		Macro macro = getMacro(name);
		if (macro != null) {
			setMacro(macro);
		}
	}
	public void setMacro(Macro macro) {
		currentMacro = macro;
	}
	public void setAndRunMacro(String name) {
		Macro macro = getMacro(name);
		if (macro != null) {
			setMacro(macro);
			runMacro();
		}
	}
	
	
	public void setNextAngleMode() { angleMode = angleMode.next(); }
	public void setEntryClears(boolean b) { isEntryClears = b; }
	public void setEntryEditable(boolean b) { isEntryEditable = b; }

	public Num toRadians(Num num) {
		return num.div(new NumDouble(angleMode.getFactor()));
	}
	
	public Num fromRadians(Num num) {
		return num.mult(new NumDouble(angleMode.getFactor()));
	}
	
	public void setMacroName(String name) {
		currentMacro.setName(name);
	}
	
	public void runMacro() {
		if (currentMacro != null) {
			apply(currentMacro);
		}
	}
	
	public void startMacro() {
		currentMacro = new Macro();
		isRecording = true;
	}

	public void stopMacro() {
		isRecording = false;
		macroList.add(currentMacro);
	}
	
	public String getMacroListing() {
		StringBuilder sb = new StringBuilder();
		sb.append("MACROS: \n\n");
		if (macroList.size() == 0) {
			sb.append("[no macros]");
		} else {
			for (Macro macro : macroList) {
				sb.append(macro.toString() + "\n");
			}
		}
		return sb.toString();
	}
	
	public String getCommandListing() {
		StringBuilder sb = new StringBuilder();
		sb.append("COMMANDS: \n\n");
		if (commandList == null || commandList.size() == 0) {
			sb.append("[no commands]");
		} else {
			for (Command command : commandList) {
				sb.append(command.getAbbrev() + "\n");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----Macros----\n");
		for (Macro macro : macroList) {
			sb.append(macro.toString());
		}
		sb.append("----Stack----\n");
		sb.append(stack.toString());
		return sb.toString();
	}
	
	@Override
	public Boolean apply(Command command) {
		if (command == null) {
			status = ERROR_MESSAGE;
			return false;
		} 
		
		if (command.isRecordable()) {
			if (isRecording && !command.isMacro()) {
				currentMacro.addCommand(command);
			}
			if (commandList == null) {
				commandList = new ArrayList<>();
			}
			commandList.add(command);
		}
		boolean success = command.apply(this);

		if (command.isRecordable()) {
			status = success ? command.getAbbrev() : ERROR_MESSAGE;
		}
		return success; 
	}
}
