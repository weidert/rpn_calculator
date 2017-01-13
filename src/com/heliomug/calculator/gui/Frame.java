package com.heliomug.calculator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.heliomug.calculator.Calculator;
import com.heliomug.calculator.Command;
import com.heliomug.calculator.Macro;
import com.heliomug.calculator.StandardCommand;
import com.heliomug.utils.FileUtils;

@SuppressWarnings("serial")
public class Frame extends JFrame implements Consumer<Command> {
	
	public static final Color TEXT_COLOR = new Color(255, 255, 255);
	public static final Color BACKGROUND_COLOR = new Color(0, 0, 127);
	public static final Color BORDER_COLOR = Color.BLACK;

	public static final Font TEXT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 24);
	public static final Font MESSAGE_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	public static final Font BIG_BUTTON_FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);
	public static final Font SMALL_BUTTON_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	public static final Font SHORTCUT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 10);

	public static final int DECIMAL_PLACES_TO_SHOW = 4;
	public static final int STACK_LINES = 4;
	public static final String EMPTY_STRING = "---";
	
	private static final int MODE_STANDARD = 0;
	private static final int MODE_COMMAND = 1;
	private static final int MODE_MACRO_NAME = 2;
	
	private static Frame theFrame;
	
	public static Frame getFrame() {
		if (theFrame == null) {
			theFrame = new Frame();
		}
		return theFrame;
	}

	public static Calculator getCalculator() {
		return getFrame().calculator;
	}
	
	
	private File lastSaveFile;
	
	private int mode;
	
	private String currentString;

	private Calculator calculator;
	private boolean showCalculator;
	
	private Frame() {
		super("RPN Calculator");

		calculator = new Calculator();
		currentString = "";
		mode = MODE_STANDARD;
		lastSaveFile = null;
		showCalculator = true;
		
		setJMenuBar(new MenuBar());

		URL url = Frame.class.getResource("/heliomug256.png");
		setIconImage(new ImageIcon(url).getImage());
		
		add(makeTabbedPane());
		
		pack();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyEvent(e);
			}
		});
		setResizable(false);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JTabbedPane makeTabbedPane() {
		JTabbedPane tabbedPane = new JTabbedPane() {
			@Override
			public void paint(Graphics g) {
				if (showCalculator) {
					setSelectedIndex(0);
					showCalculator = false;
				}
				super.paint(g);
			}
		};
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		
		tabbedPane.setFocusable(false);
		
		tabbedPane.addTab("Calculator", null, makeCalculatorPanel(), "The Calculator");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_C);
		tabbedPane.addTab("Macros", null, new PanelMacro(), "List of Macros");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_M);
		tabbedPane.addTab("Command History", null, new PanelCommandHistory(), "Record of every command executed");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_H);

		tabbedPane.addChangeListener((ChangeEvent e) -> {
			int index = tabbedPane.getSelectedIndex();
			if (index == 1 || index == 2) {
				PanelUpdateable panel = (PanelUpdateable)tabbedPane.getSelectedComponent();
				panel.update();
			}
		});

		return tabbedPane;
	}

	private JPanel makeCalculatorPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new PanelStack(), BorderLayout.NORTH);
		panel.add(new PanelKeyboard(), BorderLayout.CENTER);
		panel.add(new PanelStatus(), BorderLayout.SOUTH);

		return panel;
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
					Command command = KeyMap.getCommand(stroke);
					// might be null
					accept(command);
				}
			} else {
				if (keyCode == KeyEvent.VK_ENTER) {
					processString();
				} else if (keyCode == KeyEvent.VK_TAB) {
					autoComplete(currentString);
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

	private void returnToStandardMode() {
		mode = MODE_STANDARD;
		currentString = "";
	}
	
	private void processString() {
		if (mode == MODE_MACRO_NAME) {
			Macro macro = getCalculator().getCurrentMacro();
			if (macro != null) {
				macro.setName(currentString);
			}
		} else if (mode == MODE_COMMAND) {
			Command command = StandardCommand.getCommand(currentString);
			if (command != null) {
				accept(command);
			} else {
				Macro macro = getCalculator().lookupMacro(currentString);
				getCalculator().setAndRunMacro(macro);
			}
		} 
		returnToStandardMode();
	}

	private void autoComplete(String prefix) {
		List<Command> commands = findCommands(currentString);
		if (commands.size() == 0) {
			currentString = "";
			returnToStandardMode();
			String message = String.format("No commands begin with \"%s\"", prefix);
			JOptionPane.showMessageDialog(this, message, "Multiple Options", JOptionPane.INFORMATION_MESSAGE);
		} else if (commands.size() == 1) {
			currentString = commands.get(0).getName();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("More than one option:\n");
			for (Command command : commands) {
				sb.append(command.getName() + "\n");
			}
			JOptionPane.showMessageDialog(this, sb.toString(), "Multiple Options", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private List<Command> findCommands(String prefix) {
		List<Command> li = new ArrayList<>();
		li.addAll(StandardCommand.getCandidates(prefix));
		for (Macro macro : getCalculator().getMacroList()) {
			if (macro.hasPrefix(prefix)) {
				li.add(macro);
			}
		}
		return li;
	}
	
	public String getCurrentString() { 
		if (currentString.length() == 0) {
			if (mode == MODE_COMMAND) {
				return "command or macro?";
			} else if (mode == MODE_MACRO_NAME) {
				return "macro name?";
			}
		}
		return currentString; 
	}
	
	public void showCalculator() {
		showCalculator = true;
		repaint();
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
		showCalculator();
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
		if (command != null) {
			showCalculator();
		}
	}
}
