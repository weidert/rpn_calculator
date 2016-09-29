package com.heliomug.java.rpncalc;

import java.io.Serializable;

public abstract class Saveable implements Serializable {
	private static final long serialVersionUID = -952778832295049132L;

	abstract String getFileDescription();
	abstract String getFileExtension();
	
	public boolean save() {
		return FileHandler.saveObject(this);
	}
}
