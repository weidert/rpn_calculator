package com.heliomug.java.rpncalc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public  class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 5010101302743898796L;

	private final Border BORDER = BorderFactory.createLineBorder(Color.BLACK, 2); 
	private final Color SHIFT_COLOR = new Color(0, 192, 0);
	private final Color INACTIVE_SHIFT_COLOR = new Color(0, 128, 0);
	private final Color COMMAND_COLOR = new Color(0, 0, 192);

	private final Font BIG_FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);
	private final Font SMALL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	
	private boolean shiftOn;
	
	private Calculator calculator;
	
	public ButtonPanel(Calculator c) {
		calculator = c;
		shiftOn = false;
		this.setFocusable(false);
		
		setupGUI();
	}
	
	private List<Command[][]> getStandardCommands() {
		List<Command[][]> commands = new ArrayList<Command[][]>();
		
		commands.add(new Command[][] 
				{{StandardCommand.MACRO_RUN, StandardCommand.MACRO_START_STOP, StandardCommand.COMBO, StandardCommand.STORE, StandardCommand.INSERT_7, StandardCommand.INSERT_8, StandardCommand.INSERT_9, StandardCommand.DIVIDE}, 
				{StandardCommand.MACRO_NAME_START, StandardCommand.MACRO_DELETE, StandardCommand.PERM, StandardCommand.RAND}});
		commands.add(new Command[][] 
				{{StandardCommand.LN, StandardCommand.EXP, StandardCommand.SQRT, StandardCommand.POW, StandardCommand.INSERT_4, StandardCommand.INSERT_5, StandardCommand.INSERT_6, StandardCommand.MULTIPLY}, 
				{StandardCommand.LOG_10, StandardCommand.MAGNITUDE, StandardCommand.ROOT, StandardCommand.SQUARE}});
		commands.add(new Command[][]
				{{StandardCommand.SIN, StandardCommand.COS, StandardCommand.TAN, StandardCommand.FLIP, StandardCommand.INSERT_1, StandardCommand.INSERT_2, StandardCommand.INSERT_3, StandardCommand.SUBTRACT}, 
				{StandardCommand.ASIN, StandardCommand.ACOS, StandardCommand.ATAN, null}}); // reserved for i
		commands.add(new Command[][] 
				{{StandardCommand.CLEAR_ENTRY, StandardCommand.SWAP, StandardCommand.PI, StandardCommand.NEG, StandardCommand.INSERT_0, StandardCommand.ADD_DECIMAL, StandardCommand.BACKSPACE, StandardCommand.ADD}, 
				{StandardCommand.CLEAR_STACK, StandardCommand.CYCLE}});
		commands.add(new Command[][]
				{{new ShiftCommand(), StandardCommand.UNDO, StandardCommand.DROP, StandardCommand.ENTER}, 
				{}});
		return commands;
	}

	private void setupGUI() {
		List<Command[][]> commands = getStandardCommands();
		
		this.setBackground(Color.BLACK);
		this.setLayout(new GridLayout(0, 1));
		for (Command[][] commandRow : commands) {
			JPanel rowPanel = new JPanel();
			rowPanel.setLayout(new GridLayout(1, 0));
			for (int j = 0 ; j < commandRow[0].length ; j++) {
				if (commandRow[0][j] != null) {
					if (commandRow[0][j] instanceof ShiftCommand) {
						rowPanel.add(new ShiftPanel());
					} else if (j < commandRow[1].length && commandRow[1][j] != null) {
						rowPanel.add(new Button(commandRow[0][j], commandRow[1][j]));
					} else { 
						rowPanel.add(new Button(commandRow[0][j]));
					}
				} else {
					rowPanel.add(new JPanel());
				}
			}
			this.add(rowPanel);
		}
	}
	
	private class Button extends JButton {
		private static final long serialVersionUID = 2108578019121145854L;

		private Command primary;
		private Command secondary;

		public Button() {
			this.setFocusable(false);
			this.setBorder(BORDER);
			this.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (ButtonPanel.this.shiftOn || SwingUtilities.isRightMouseButton(e)) {
						if (secondary != null) {
							calculator.executeCommand(secondary);
						} else {
							calculator.executeCommand(primary);
						}
						shiftOn = false;
					} else {
						calculator.executeCommand(primary);
					}
				}
			});
		}
		
		public Button(Command std) {
			this();
			this.setLayout(new BorderLayout());
			
			if (std instanceof ShiftCommand) {
				this.setBackground(SHIFT_COLOR);
			} 

			this.primary = std;

			this.add(makeCommandPanel(primary, true), BorderLayout.CENTER);
		}
		
		public Button(Command std, Command shift) {
			this(std);
			if (shift != null) { 
				this.secondary = shift;
				this.add(makeCommandPanel(secondary, false), BorderLayout.SOUTH);
			}
		}
		
		public void paint(Graphics g) {
			if (shiftOn) {
				
			}
			super.paint(g);
		}

		public JPanel makeCommandPanel(Command cmd, boolean big) {
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			JLabel label;
			label = new JLabel(cmd.getButtonString());
			if (big) { 
				label.setFont(BIG_FONT); 
				panel.setOpaque(false);
			} else {
				label.setFont(SMALL_FONT);
				panel.setBackground(SHIFT_COLOR);
			}
			label.setHorizontalAlignment(JLabel.CENTER);
			panel.add(label, BorderLayout.CENTER);
			
			String shortcut = calculator.getKeyMap().getKeyStrokeText(cmd);
			
			if (shortcut.length() > 0) {
				label = new JLabel(" " + shortcut + " ");
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setForeground(COMMAND_COLOR);
				label.setFont(SMALL_FONT);
				panel.add(label, BorderLayout.EAST);
			}
			return panel;
		}
		
	}

	private class ShiftPanel extends JPanel {
		private static final long serialVersionUID = -2472598694906672237L;
		
		Command command;
		
		public ShiftPanel() {
			super();
			JLabel label = new JLabel("SHIFT");
			label.setFont(BIG_FONT);
			command = new ShiftCommand();
			this.setBorder(BORDER);
			this.add(label);
			this.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					command.run(calculator);
				}
			});
		}
		
		public void paint(Graphics g) {
			if (ButtonPanel.this.shiftOn) {
				this.setBackground(SHIFT_COLOR);
			} else {
				this.setBackground(INACTIVE_SHIFT_COLOR);
			}
			super.paint(g);
		}
	}
	
	private class ShiftCommand implements Command {
		@Override
		public boolean run(Calculator c) {
			ButtonPanel.this.shiftOn = !ButtonPanel.this.shiftOn;
			c.getDisplay().repaint();
			return true;
		}

		@Override public boolean isRecordable() { return false; }
		@Override public String getAbbreviation() {	return "<shft>"; }
		@Override public String getButtonString() {	return "shift"; }
		@Override public String getName() {	return "Shift";	}
	}
}
