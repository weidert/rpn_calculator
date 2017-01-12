package com.heliomug.calculator.gui;

import java.awt.EventQueue;

public class MainCalc {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Frame frame = Frame.getFrame();
			frame.setVisible(true);
		});
	}
}
