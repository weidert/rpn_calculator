package com.heliomug.calculator.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.heliomug.calculator.Command;

@SuppressWarnings("serial")
public class PanelCommandHistory extends PanelUpdateable {
	boolean needsUpdate;
	
	public PanelCommandHistory() {
		super();
		needsUpdate = true;
		
		setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea() {
			@Override
			public void paint(Graphics g) {
				if (needsUpdate) {
					setText(getString());
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
	
	public String getString() {
		List<Command> commandList = Frame.getCalculator().getCommandHistory();
		StringBuilder sb = new StringBuilder();
		sb.append("COMMANDS: \n\n");
		if (commandList == null || commandList.size() == 0) {
			sb.append("[no commands]");
		} else {
			for (Command command : commandList) {
				sb.append(command.getAbbrev() + "\n");
			}
		}
		return sb.toString();
	}
	
	public void update() {
		needsUpdate = true;
	}

}
