package com.heliomug.java.rpncalc;

import java.io.Serializable;
import java.util.ArrayList;

public class Macro implements Command, Serializable {
	private static final long serialVersionUID = -3737603284393866499L;

	private ArrayList<Command> commands;
	private String name;
	private String comment;
	
	public Macro() {
		commands = new ArrayList<Command>();
		name = null;
		comment = "";
	}
	
	public Macro(Num n) {
		this();
		addCommand(new InsertNum(n));
	}
	
	// getters
	public String getComment() { return this.comment; }
	public String getName() { return this.name; }
	public String getAbbreviation() { return this.name; }
	public String getButtonString() { return this.name; }
	public boolean isRecordable() { return true; }
	public String toString() { return String.format("%s: %s", name, commands.toString()); }
	public ArrayList<Command> getCommands() { return commands; }
	
	// setters
	public void setComment(String str) { this.comment = str; }
	public void setName(String name) { this.name = name; }
	public void addCommand(Command c) { commands.add(c); }
	
	// runners
	public boolean run(Calculator c) {
		Stack tempStack = c.getStack().copy();
		boolean toRet = true;
		for (Command command : commands) {
			if (!command.run(c)) toRet = false;
		}
		if (toRet == false) c.setStack(tempStack);
		return toRet;
	}

	public static class InsertNum implements Command, Serializable {
		private static final long serialVersionUID = 7291666620095118172L;

		private Num num;

		public InsertNum(Num n) { this.num = n; }
		
		public boolean isRecordable() { return true; }
		public String getName() { return "<insert " + num.toString() + ">"; }
		public String toString() { return getName(); }
		public String getButtonString() { return getName(); }
		public String getAbbreviation() { return "< mr >"; }
		
		public boolean run(Calculator c) {
			c.getStack().push(num);
			return true;
		}

	}


}
