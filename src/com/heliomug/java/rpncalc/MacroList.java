package com.heliomug.java.rpncalc;

import java.util.ArrayList;
import java.util.Iterator;

public class MacroList extends Saveable implements Iterable<Macro> {
	private static final long serialVersionUID = -3275032159883325708L;
	public static final String FILE_DESC = "RPN Macros";
	public static final String FILE_EXT = "rpnmacros";

	public String getFileDescription() { return FILE_DESC; }
	public String getFileExtension() { return FILE_EXT; }


	private ArrayList<Macro> availableMacros;

	
	public MacroList() {
		availableMacros = new ArrayList<Macro>();
	}
	
	// getters
	public int getSize() { return availableMacros.size(); }
	public String toString() { return availableMacros.toString(); }

	public Macro getMacro(String name) {
		for (Macro m : availableMacros) {
			if (m != null && m.getName() != null && m.getName().equals(name)) return m;
		}
		return null;
	}

	// setters
	public void addMacroToAvailable(Macro m) {
		String incomingName = m.getName();
		Macro existing = getMacro(incomingName);
		if (existing != null) {
			availableMacros.remove(existing);
		}
		availableMacros.add(m);
	}

	public boolean removeMacroFromAvailable(Macro m) {
		return availableMacros.remove(m);
	}

	// savers / loaders
	public boolean load() {
		Object loadedObject = FileHandler.loadObject("Macro List", "rpnmacros"); 
		if (loadedObject != null && loadedObject.getClass() == availableMacros.getClass()) {
			@SuppressWarnings("unchecked")
			ArrayList<Macro> loadedMacros = (ArrayList<Macro>)loadedObject;
			if (loadedMacros != null && loadedMacros.size() > 0) { 
				for (Macro macro : loadedMacros) {
					addMacroToAvailable(macro);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean save() { 
		if (availableMacros.size() > 0) {
			return super.save();
		} else {
			return false;
		}
	}
	
	@Override
	public Iterator<Macro> iterator() {
		return this.availableMacros.iterator();
	}
}
