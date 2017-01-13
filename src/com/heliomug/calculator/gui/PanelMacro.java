package com.heliomug.calculator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.heliomug.calculator.Macro;

@SuppressWarnings("serial")
public class PanelMacro extends PanelUpdateable {
	private JPanel panel;
	
	public PanelMacro() {
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	
	public void update() {
		panel.removeAll();
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		GridBagConstraints c = new GridBagConstraints();

		List<Macro> macroList = Frame.getCalculator().getMacroList();
		if (macroList.size() == 0) {
			panel.add(new JLabel("no macros yet!"));
		} else {
			for (int i = 0 ; i < macroList.size() ; i++) {
				Macro macro = macroList.get(i);
				c.gridy = i;
				c.fill = GridBagConstraints.BOTH;
				c.gridx = 0;
				JLabel label = new JLabel(String.format(" %s ", macro.getName())); 
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				panel.add(label, c);
				c.gridx = 1;
				panel.add(new MacroButton("Run", (ActionEvent e) -> {
					Frame.getCalculator().setAndRunMacro(macro);
					Frame.getFrame().showCalculator();
				}), c);
				c.gridx = 2;
				panel.add(new MacroButton("Show", (ActionEvent e) -> {
					String listing = macro.getListing();
					String title = "Listing for Macro " + macro.getName();
					JOptionPane.showMessageDialog(Frame.getFrame(), listing, title, JOptionPane.INFORMATION_MESSAGE);
				}), c);
				c.gridx = 3;
				panel.add(new MacroButton("Rename", (ActionEvent e) -> {
					String name = (String)JOptionPane.showInputDialog(Frame.getFrame(), "New name?", macro.getName());
					if (!Frame.getCalculator().commandExists(name)) {
						macro.setName(name.trim());
						PanelMacro.this.update();
					}
				}), c);
				c.gridx = 4;
				panel.add(new MacroButton("Delete", (ActionEvent e) -> {
					String message = "Are you sure you want to delete macro " + macro.getName() + "?";
					int response = JOptionPane.showConfirmDialog(Frame.getFrame(), message);
					if (response == JOptionPane.YES_OPTION) {
						Frame.getCalculator().removeMacro(macro);
					}
					PanelMacro.this.update();
				}), c);
			}
		}
		panel.revalidate();
	}
	
	private class MacroButton extends JButton {
		public MacroButton(String text, ActionListener al) {
			super(text);
			setFont(Frame.SMALL_BUTTON_FONT);
			addActionListener(al);
			setFocusable(false);
		}
	}
}


