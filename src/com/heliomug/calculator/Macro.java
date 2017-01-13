package com.heliomug.calculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Macro implements Command, Serializable {
	private static final long serialVersionUID = 4612034564958818436L;

	private static final String DEFAULT_NAME = "[no name]";
	private String name;
	private List<Command> commands;

	public Macro() {
		name = DEFAULT_NAME;
		commands = new ArrayList<>();
		commands.add(StandardCommand.SEAL);
	}
	
	public Macro(double constant) {
		this();
		commands.add(new Command() {
			private static final long serialVersionUID = 3463012517449651727L;

			@Override
			public boolean apply(Calculator calc) {
				calc.getStack().push(new NumDouble(constant));
				calc.setEntryClears(false);
				calc.setEntryEditable(false);
				return true;
			}

			@Override
			public String getName() {
				return String.valueOf(constant);
			}

			@Override
			public String getAbbrev() {
				return String.format("<%s>", getName());
			}

			@Override
			public boolean hasName(String name) {
				return false;
			}

			@Override
			public boolean isMacro() {
				return false;
			}

			@Override
			public boolean isRecordable() {
				return false;
			}

			@Override
			public boolean hasPrefix(String prefix) {
				return false;
			}
			
			@Override
			public String toString() {
				return getAbbrev();
			}
			
		});
	}
	
	@Override
	public boolean apply(Calculator calc) {
		boolean success = true;
		for (Command command : commands) {
			if (!calc.apply(command)) {
				success = false;
				break;
			}
		}
		return success;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getAbbrev() {
		return name;
	}
	
	@Override
	public boolean hasName(String name) {
		return name.equals(name);
	}
	
	@Override
	public boolean hasPrefix(String prefix) {
		return name.startsWith(prefix);
	}
	
	@Override
	public boolean isMacro() {
		return true;
	}
	
	@Override
	public boolean isRecordable() {
		return false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addCommand(Command com) {
		commands.add(com);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("----%s----\n", name));
		for (Command command : commands) {
			sb.append(String.format("\t%s\n", command));
		}
		return sb.toString();
	}
}
