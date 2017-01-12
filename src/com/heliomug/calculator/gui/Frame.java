package com.heliomug.calculator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import com.heliomug.calculator.Calculator;
import com.heliomug.calculator.Command;
import com.heliomug.calculator.Stack;
import com.heliomug.calculator.StandardCommand;
import com.heliomug.utils.FileUtils;

@SuppressWarnings("serial")
public class Frame extends JFrame implements Consumer<Command> {
	private static final Color TEXT_COLOR = new Color(255, 255, 255);
	private static final Color BACKGROUND_COLOR = new Color(0, 0, 127);
	private static final Color BORDER_COLOR = Color.BLACK;
	public static final Font TEXT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 24);
	public static final Font MESSAGE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

	public static final int DECIMAL_PLACES_TO_SHOW = 4;
	
	private static final String EMPTY_STRING = "---";
	
	private static final int MODE_STANDARD = 0;
	private static final int MODE_COMMAND = 1;
	private static final int MODE_MACRO_NAME = 2;
	
	private static final int STACK_LINES = 4;
	
	private static Frame theFrame;
	
	public static Frame getFrame() {
		if (theFrame == null) {
			theFrame = new Frame();
		}
		return theFrame;
	}

	private File lastSaveFile;
	
	private int mode;
	
	private String currentString;

	private Calculator calculator;
	private KeyMap keyMap;
	
	private Frame() {
		super("RPN Calculator");

		calculator = new Calculator();
		keyMap = KeyMap.getKeyMap();
		currentString = "";
		mode = MODE_STANDARD;
		lastSaveFile = null;
		
		setJMenuBar(new MenuBar());

		add(getTabbedPanel());
		
		pack();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyEvent(e);
			}
		});
		setResizable(false);
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JTabbedPane getTabbedPanel() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		tabbedPane.setFocusable(false);
		
		tabbedPane.addTab("Calculator", null, getCalculatorPanel(), "The Calculator");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_C);
		tabbedPane.addTab("Macros", null, getMacroPanel(), "List of Macros");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_M);
		tabbedPane.addTab("Command History", null, getCommandHistoryPanel(), "Record of every command executed");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_H);

		tabbedPane.addChangeListener((ChangeEvent e) -> {
			int index = tabbedPane.getSelectedIndex();
			if (index == 1 || index == 2) {
				TextPanel panel = (TextPanel)tabbedPane.getSelectedComponent();
				panel.update();
			}
		});

		return tabbedPane;
	}

	private JPanel getCalculatorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getStackPanel(), BorderLayout.NORTH);
		panel.add(new KeyboardPanel(), BorderLayout.CENTER);
		panel.add(getMessagePanel(), BorderLayout.SOUTH);

		return panel;
	}
	
	private JPanel getMessagePanel() {
		JPanel panel = new JPanel(); 
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		

		Label label;
		JPanel subpanel;

		subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		label = new StatusLabel(() -> getCalculator().getAngleMode().getLetter());
		subpanel.add(label, BorderLayout.WEST);
		label = new StatusLabel(() -> String.format("%5s", getCalculator().getStatus()));
		subpanel.add(label, BorderLayout.EAST);
		panel.add(subpanel, BorderLayout.WEST);

		subpanel = new JPanel();
		subpanel.setLayout(new BorderLayout());
		label = new StatusLabel(() -> "macro: " + getCalculator().getCurrentMacroName());
		subpanel.add(label, BorderLayout.WEST);
		label = new StatusLabel(() -> {
			if (currentString.length() == 0) {
				if (mode == MODE_COMMAND) {
					return "command or macro?";
				} else if (mode == MODE_MACRO_NAME) {
					return "macro name?";
				}
			}
			return currentString;
		});
		subpanel.add(label, BorderLayout.CENTER);
		panel.add(subpanel, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel getStackPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(BACKGROUND_COLOR);
		panel.setLayout(new GridLayout(0, 1));

		for (int i = STACK_LINES - 1 ; i >= 0; i--) {
			int index = i;
			Supplier<String> sup = () -> {
				Stack stack = getCalculator().getStack();
				if (stack.size() <= index) {
					return String.format("%s", EMPTY_STRING);
				} else {
					if (getCalculator().isEntryEditable() && index == 0) {
						return stack.get(index).toString();
					} else {
						return stack.get(index).toStringWithDec(DECIMAL_PLACES_TO_SHOW);
					}
				}
			};
			Label label = new Label(sup);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setForeground(TEXT_COLOR);
			label.setFont(TEXT_FONT);
 			panel.add(label);
		}
		
		return panel;
	}

	private JPanel getMacroPanel() {
		return new TextPanel(() -> getCalculator().getMacroListing());
	}
	
	private JPanel getCommandHistoryPanel() {
		return new TextPanel(() -> getCalculator().getCommandListing());
	}
	
	private void handleKeyEvent(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_D && e.isControlDown()) {
			exit();
		} else if (keyCode == KeyEvent.VK_SEMICOLON) {
			mode = MODE_COMMAND;
		} else if (keyCode  == KeyEvent.VK_SLASH && !e.isShiftDown()) {
			mode = MODE_MACRO_NAME;
		} else if (keyCode  == KeyEvent.VK_ESCAPE) {
			returnToStandardMode();
		} else {
			if (mode == MODE_STANDARD) {
				// no control characters (shift, etc) allowed by themselves
				if ((keyCode >= 32 && keyCode < 256) || keyCode == 10 || keyCode == 8) {
					KeyStroke stroke = KeyStroke.getKeyStroke(keyCode, e.getModifiers());
					Command command = keyMap.getCommand(stroke);
					// might be null
					accept(command);
				}
			} else {
				if (keyCode == KeyEvent.VK_ENTER) {
					processString();
				} else {
					char letter = e.getKeyChar();
					//letter = Character.toLowerCase(letter);
					if (Character.isLetter(letter) || letter == KeyEvent.VK_SPACE) {
						currentString += letter;
					} else if (letter == KeyEvent.VK_BACK_SPACE && currentString.length() > 0) {
						currentString = currentString.substring(0, currentString.length() - 1);
					} else if (letter == KeyEvent.VK_DELETE) {
						currentString = "";
					}
				}
			}
		}
		repaint();
	}

	private Calculator getCalculator() {
		return calculator;
	}
	
	
	private void returnToStandardMode() {
		mode = MODE_STANDARD;
		currentString = "";
	}
	
	private void processString() {
		if (mode == MODE_MACRO_NAME) {
			getCalculator().setMacroName(currentString);
		} else if (mode == MODE_COMMAND) {
			Command command = getCommand(currentString);
			if (command != null) {
				accept(command);
			} else {
				getCalculator().setAndRunMacro(currentString);
			}
		} 
		returnToStandardMode();
	}
	
	
	private Command getCommand(String string) {
		return StandardCommand.getCommand(string);
	}
	
	
	public void load() {
		try {
			Calculator calc = (Calculator) FileUtils.readObject("Calculator", "calc");
			if (calc != null) {
				calculator = calc;
				repaint();
			}
		} catch (FileNotFoundException e) {
			String message = "File not found!  Sorry!";
			JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			String message = "That doesn't seem to be a calculator!  Sorry!";
			JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			String message = "IO Exception!  Sorry!";
			JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void save() {
		if (lastSaveFile == null) {
			saveAs();
		} else {
			try {
				FileUtils.saveObject(calculator, lastSaveFile);
			} catch (FileNotFoundException e) {
				String message = "File not found!  Sorry!";
				JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				String message = "IO Exception!  Sorry!";
				JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void saveAs() {
		try {
			FileUtils.saveObject(calculator, "Calculator", "calc");
		} catch (IOException e) {
			String message = "Could not save calculator";
			JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void exit() {
		accept(StandardCommand.EXIT);
	}
	
	
	@Override
	public void accept(Command command) {
		calculator.apply(command);
		repaint();
	}

	private class TextPanel extends JPanel {
		Supplier<String> updater;
		boolean needsUpdate;
		
		public TextPanel(Supplier<String> sup) {
			super();
			updater = sup;
			needsUpdate = true;
			
			setLayout(new BorderLayout());
			JTextArea textArea = new JTextArea() {
				@Override
				public void paint(Graphics g) {
					if (needsUpdate) {
						setText(updater.get());
						needsUpdate = false;
					}
					super.paint(g);
				}
			}; 
			textArea.setEditable(false);
			textArea.setFont(MESSAGE_FONT);
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
	
	private class StatusLabel extends Label {
		public StatusLabel(Supplier<String> sup) {
			super(() -> String.format(" %s ", sup.get()));
			setHorizontalAlignment(SwingConstants.RIGHT);
			setFont(MESSAGE_FONT);
			setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
		}
	}
}
