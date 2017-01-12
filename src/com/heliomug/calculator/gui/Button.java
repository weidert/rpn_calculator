package com.heliomug.calculator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.heliomug.calculator.Command;

@SuppressWarnings("serial")
public class Button extends JButton {
	private static final Font BIG_FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);
	private static final Font SMALL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	private static final Font SHORTCUT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	private static final Color SHORTCUT_COLOR = Color.BLUE;
	private static final Color ALT_COLOR = new Color(0, 191, 0);
	
	public Button(Command command) {
		this(command, command == null ? "" : command.getAbbrev());
	}
	
	public Button(Command command, String text) {
		this(command, text, null, null);
	}
	
	public Button(Command command, String text, Command alternate, String altText) {
		super();
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setLayout(new BorderLayout());

		add(getPrimaryPanel(command, text), BorderLayout.CENTER);
		if (alternate != null) {
			add(getAlternatePanel(alternate, altText), BorderLayout.SOUTH);
		}
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && alternate != null) {
					Frame.getFrame().accept(alternate);
				} else if (command != null) {
					Frame.getFrame().accept(command);
				}
			}
		});
		setFocusable(false);
	}
	
	public JPanel getPrimaryPanel(Command command, String text) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);

		JLabel label;

		label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(BIG_FONT);
		panel.add(label, BorderLayout.CENTER);
		
		label = new JLabel(" " + KeyMap.getKeyMap().getShortcut(command) + " ");
		label.setFont(SHORTCUT_FONT);
		label.setForeground(SHORTCUT_COLOR);
		panel.add(label, BorderLayout.EAST);
		
		return panel;
	}
	
	public JPanel getAlternatePanel(Command command, String text) {
		JPanel panel = new JPanel();
		panel.setBackground(ALT_COLOR);
		panel.setLayout(new BorderLayout());
		panel.setOpaque(true);

		JLabel label;
		
		label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(SMALL_FONT);
		panel.add(label, BorderLayout.CENTER);
		
		label = new JLabel(" " + KeyMap.getKeyMap().getShortcut(command) + " ");
		label.setFont(SHORTCUT_FONT);
		label.setForeground(SHORTCUT_COLOR);
		panel.add(label, BorderLayout.EAST);
		return panel;
	}
}
