package com.heliomug.java.rpncalc;

import java.awt.EventQueue;

public class MainRPNCalculator {
	public static void main(String [] args) {
		EventQueue.invokeLater(new Runnable() { 
			public void run() {
				new Calculator();
			}
		});
	}
}
