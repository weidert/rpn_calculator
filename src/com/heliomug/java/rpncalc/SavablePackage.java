package com.heliomug.java.rpncalc;

public class SavablePackage extends Saveable {
	private static final long serialVersionUID = 6001806658345612375L;
	
	private static final String FILE_DESC = "RPN Calculator";
	private static final String FILE_EXT = "rpn";

	private CalculatorSettings calculatorSettings;
	private DisplaySettings displaySettings;
	private Stack stack;
	private MacroList macros;
	private Macro activeMacro;
	
	public SavablePackage(CalculatorSettings calculatorSettings, DisplaySettings frameSettings, Stack stack, MacroList macros, Macro activeMacro) {
		this.calculatorSettings = calculatorSettings;
		this.displaySettings = frameSettings;
		this.stack = stack;
		this.macros = macros;
		this.activeMacro = activeMacro;
	}
	
	public CalculatorSettings getCalculatorSettings() { return this.calculatorSettings; }
	public DisplaySettings getFrameSettings() { return this.displaySettings; }
	public Stack getStack() { return this.stack; }
	public MacroList getMacroList() { return this.macros; }
	public Macro getActiveMacro() { return this.activeMacro; }
	public String getFileExtension() { return FILE_EXT; }
	public String getFileDescription() { return FILE_DESC; }
	
	
	public static SavablePackage loadDefault() {
		SavablePackage settings = (SavablePackage) FileHandler.loadHere(".rpn");
		return settings;
	}
	
	public void saveDefault() {
		FileHandler.saveHere(this, ".rpn");
	}
	
	public static SavablePackage load() {
		Object loadedObject = FileHandler.loadObject("RPN Calculator", "rpn"); 
		if (loadedObject != null && loadedObject.getClass() == SavablePackage.class) {
			SavablePackage loadedStuff = (SavablePackage) loadedObject;
			return loadedStuff;
		}
		return null;
	}
}
