package com.heliomug.calculator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	public MenuBar() {
		super();
		JMenu menu;
		JMenuItem item;
		
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		item = new JMenuItem("Open", KeyEvent.VK_O);
		item.addActionListener((ActionEvent e) -> Frame.getFrame().open());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		menu.add(item);
		item = new JMenuItem("Save", KeyEvent.VK_S);
		item.addActionListener((ActionEvent e) -> Frame.getFrame().save());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		menu.add(item);
		item = new JMenuItem("Save As", KeyEvent.VK_A);
		item.addActionListener((ActionEvent e) -> Frame.getFrame().saveAs());
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Load Macros", KeyEvent.VK_L);
		item.addActionListener((ActionEvent e) -> Frame.getFrame().loadMacros());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("About", KeyEvent.VK_B);
		item.addActionListener((ActionEvent e) -> {
			String message = "Craig Weidert, 2017\nheliomug.com";
			JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
		});
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Exit", KeyEvent.VK_X);
		item.addActionListener((ActionEvent e) -> Frame.getFrame().exit());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		menu.add(item);
		add(menu);
	}
}
