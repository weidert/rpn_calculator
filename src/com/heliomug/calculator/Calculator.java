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
	
	transient private List<Command> commandHistory;
	
	transient private String status;
	
	public Calculator() {
		stack = new Stack();
		macroList = new ArrayList<>();
		commandHistory = new ArrayList<>();
		currentMacro = null;
		isEntryEditable = false;
		isEntryClears = true;
		isRecording = false;
		angleMode = DEFAULT_ANGLE_MODE;
		status = "";
	}
	
	
	public boolean isEntryClears() { 
		return isEntryClears; 
	}
	
	public boolean isEntryEditable() { 
		return isEntryEditable; 
	}
	
	public boolean isRecording() { 
		return isRecording; 
	}
	
	public double getTop() {
		return stack.peek().getDouble();
	}
	
	public Stack getStack() { 
		return stack; 
	}
	
	public String getStatus() { 
		return status == null ? "" : status; 
	}
	
	public AngleMode getAngleMode() { 
		return angleMode; 
	}

	public Macro getCurrentMacro() { 
		return currentMacro; 
	} 
	
	public Macro lookupMacro(String name) {
		// would be more efficient with a map, 
		// but then have to deal with renaming behind map's back
		for (Macro macro : macroList) {
			if (macro.getName().equals(name)) {
				return macro;
			}
		}
		return null;
	}
	
	public boolean commandExists(String name) {
		Command command = StandardCommand.getCommand(name);
		if (command != null || lookupMacro(name) != null) {
			return true;
		}
		return false;
	}

	public List<Command> lookupCommands(String prefix) {
		List<Command> li = new ArrayList<>();
		li.addAll(StandardCommand.getCandidates(prefix));
		for (Macro macro : getMacroList()) {
			if (macro.hasPrefix(prefix)) {
				li.add(macro);
			}
		}
		return li;
	}
	

	public List<Macro> getMacroList() { 
		return macroList; 
	}
	
	public List<Command> getCommandHistory() { 
		return commandHistory; 
	}
	
	
	public void cycleAngleMode() { 
		angleMode = angleMode.next(); 
	}
	
	public void setEntryClears(boolean b) { 
		isEntryClears = b; 
	}
	
	public void setEntryEditable(boolean b) { 
		isEntryEditable = b; 
	}

	
	public Num toRadians(Num num) {
		return num.div(new NumDouble(angleMode.getFactor()));
	}
	
	public Num fromRadians(Num num) {
		return num.mult(new NumDouble(angleMode.getFactor()));
	}

	
	public void setCurrentMacro(Macro macro) {
		currentMacro = macro;
		if (!macroList.contains(macro)) {
			macroList.add(currentMacro);
		}
	}
	
	public void runCurrentMacro() {
		if (currentMacro != null) {
			apply(currentMacro);
		}
	}
	
	public void setAndRunMacro(Macro macro) {
		if (macro != null) {
			setCurrentMacro(macro);
			runCurrentMacro();
		}
	}

	public void startMacro() {
		setCurrentMacro(new Macro());
		isRecording = true;
	}

	public void stopMacro() {
		isRecording = false;
	}
	
	public void removeMacro(Macro macro) {
		macroList.remove(macro);
		if (currentMacro == macro) {
			currentMacro = null;
		}
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
		}
		boolean success = command.apply(this);

		if (success) {
			if (command.isRecordable()) {
				if (commandHistory == null) {
					commandHistory = new ArrayList<>();
				}
				commandHistory.add(command);
				status = command.getAbbrev();
			} 
		} else {
			status = ERROR_MESSAGE;
		}
		
		return success; 
	}
}
