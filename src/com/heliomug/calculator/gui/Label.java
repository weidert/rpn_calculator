package com.heliomug.calculator.gui;

import java.awt.Graphics;
import java.util.function.Supplier;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Label extends JLabel {
	Supplier<String> textSupplier;

	public Label(Supplier<String> sup) {
		super("     ");
		
		this.textSupplier = sup;
	}

	@Override
	public void paint(Graphics g) {
		String text = textSupplier.get();
		if (text.length() == 0) {
			setText("     ");
		} else {
			setText(text);
		}
		super.paint(g);
	}
}
