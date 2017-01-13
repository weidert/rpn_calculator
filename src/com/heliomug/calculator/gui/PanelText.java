package com.heliomug.calculator.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.function.Supplier;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class PanelText extends PanelUpdateable {
	boolean needsUpdate;
	
	public PanelText(Supplier<String> sup) {
		super();
		needsUpdate = true;
		
		setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea() {
			@Override
			public void paint(Graphics g) {
				if (needsUpdate) {
					setText(sup.get());
					needsUpdate = false;
				}
				super.paint(g);
			}
		}; 
		textArea.setEditable(false);
		textArea.setFont(Frame.MESSAGE_FONT);
		textArea.setTabSize(2);
		JScrollPane scrollPane = new JScrollPane(textArea); 
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void update() {
		needsUpdate = true;
	}
}
