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
import com.heliomug.calculator.Stack;
import com.heliomug.calculator.StandardCommand;
import com.heliomug.utils.FileUtils;

@SuppressWarnings("serial")
public class Frame extends JFrame implements Consumer<Command> {
	
	public static final Color INACTIVE_COLOR = new Color(127, 127, 127);
	public static final Color ACTIVE_COLOR = new Color(200, 255, 200);
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
	
	Mode mode;
	
	private String currentString;

	private Calculator calculator;
	private boolean showCalculator;
	
	private Frame() {
		super("RPN Calculator");

		calculator = new Calculator();
		currentString = "";
		mode = Mode.STANDARD;
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
		tabbedPane.addTab("Commands", null, makeCommandHistoryPanel(), "Record of every command executed");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_D);
		tabbedPane.addTab("Stacks", null, makeStackHistoryPanel(), "Record of every stack state");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_S);

		tabbedPane.addChangeListener((ChangeEvent e) -> {
			int index = tabbedPane.getSelectedIndex();
			if (index > 0) {
				PanelUpdateable panel = (PanelUpdateable)tabbedPane.getSelectedComponent();
				panel.update();
			}
		});

		tabbedPane.setFocusable(false);
		
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
	
	private JPanel makeCommandHistoryPanel() {
		JPanel panel = new PanelText(() -> {
			List<Command> commandList = Frame.getCalculator().getCommandHistory();
			StringBuilder sb = new StringBuilder();
			sb.append("COMMAND HISTORY: \n\n");
			if (commandList == null || commandList.size() == 0) {
				sb.append("[no commands]");
			} else {
				for (Command command : commandList) {
					sb.append(command.getAbbrev() + "\n");
				}
			}
			return sb.toString();
		});
		return panel;
	}
	
	private JPanel makeStackHistoryPanel() {
		JPanel panel = new PanelText(() -> {
			List<Stack> stackList = Frame.getCalculator().getStackHistory();
			StringBuilder sb = new StringBuilder();
			sb.append("STACK HISTORY: \n\n");
			if (stackList == null || stackList.size() == 0) {
				sb.append("[no history yet]");
			} else {
				for (int i = 0 ; i < stackList.size() ; i++) {
					Stack stack = stackList.get(i);
					sb.append(String.format("STACK %d:\n%s\n", i, stack));
				}
			}
			return sb.toString();
		});
		return panel;
	}
	
	private void handleKeyEvent(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (e.isControlDown()) {
			if (keyCode == KeyEvent.VK_D) { 
				exit();
			} else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
				
			} else if (keyCode == KeyEvent.VK_PAGE_UP) {
				
			}
		} else if (keyCode == KeyEvent.VK_SEMICOLON) {
			mode = Mode.COMMAND;
		} else if (keyCode  == KeyEvent.VK_SLASH && !e.isShiftDown()) {
			if (getCalculator().getCurrentMacro() == null) {
				String message = String.format("No macro to name yet!");
				JOptionPane.showMessageDialog(this, message, "No macro!", JOptionPane.INFORMATION_MESSAGE);
			} else {
				mode = Mode.MACRO_NAME;
			}
		} else if (keyCode  == KeyEvent.VK_ESCAPE) {
			returnToStandardMode();
		} else {
			if (mode == Mode.STANDARD) {
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
		mode = Mode.STANDARD;
		currentString = "";
	}
	
	private void processString() {
		if (mode == Mode.MACRO_NAME) {
			Macro macro = getCalculator().getCurrentMacro();
			if (macro != null) {
				if (!getCalculator().commandExists(currentString)) {
					macro.setName(currentString.trim());
					returnToStandardMode();
				} else {
					String message = String.format("Name \"%s\" is already taken!", currentString);
					JOptionPane.showMessageDialog(this, message, "Name taken!", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				String message = String.format("No macro to name!");
				JOptionPane.showMessageDialog(this, message, "No macro!", JOptionPane.INFORMATION_MESSAGE);
				returnToStandardMode();
			}
		} else if (mode == Mode.COMMAND) {
			Command command = StandardCommand.getCommand(currentString);
			Macro macro = getCalculator().lookupMacro(currentString);
			if (command != null) {
				accept(command);
				returnToStandardMode();
			} else if (macro != null) {
				getCalculator().setAndRunMacro(macro);
				returnToStandardMode();
			} else {
				List<Command> commands = getCalculator().lookupCommands(currentString);
				if (commands.size() == 0) {
					String message = String.format("No commands begin with \"%s\"", currentString);
					JOptionPane.showMessageDialog(this, message, "No Commands!", JOptionPane.INFORMATION_MESSAGE);
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append("More than one command with this prefix:\n");
					for (Command com : commands) {
						sb.append(com.getName() + "\n");
					}
					JOptionPane.showMessageDialog(this, sb.toString(), "Multiple Options", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} 
	}

	private void autoComplete(String prefix) {
		List<Command> commands = getCalculator().lookupCommands(currentString);
		if (commands.size() == 0) {
			currentString = "";
			returnToStandardMode();
			String message = String.format("No commands begin with \"%s\"", prefix);
			JOptionPane.showMessageDialog(this, message, "No Commands!", JOptionPane.INFORMATION_MESSAGE);
		} else if (commands.size() == 1) {
			currentString = commands.get(0).getName();
		} else {
			currentString = longestCommonPrefix(commands).trim();
		}
	}

	
	public Mode getMode() {
		return this.mode;
	}
	
	public String getCurrentString() { 
		if (currentString.length() == 0) {
			if (mode == Mode.COMMAND) {
				return "command or macro?";
			} else if (mode == Mode.MACRO_NAME) {
				return "macro name?";
			}
		}
		return currentString; 
	}
	
	public String longestCommonPrefix(List<Command> commands) {
		if (commands.size() == 0) {
			return "";
		} else if (commands.size() == 1) {
			return commands.get(0).getName();
		} else {
			String longest = "";
			outerloop:
			for (int i = 0 ; i < commands.get(0).getName().length() ; i++) {
				char c = commands.get(0).getName().charAt(i);
				for (int j = 1 ; j < commands.size() ; j++) {
					if (commands.get(j).getName().charAt(i) != c) {
						break outerloop;
					}
				}
				longest += c;
			}
			return longest;
		}
	}
	
	public void showCalculator() {
		showCalculator = true;
		repaint();
	}
	
	
	public void loadMacros() {
		try {
			Calculator calc = (Calculator) FileUtils.readObject("Select Calculator to Load Macros from", "Calculator", "calc");
			if (calc != null) {
				calculator.addMacros(calc.getMacroList());;
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
	
	public void open() {
		try {
			Calculator calc = (Calculator) FileUtils.readObject("Select Calculator to Open", "Calculator", "calc");
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
				String message = "Calculator Saved!";
				JOptionPane.showMessageDialog(this, message, "Calculator Saved!", JOptionPane.INFORMATION_MESSAGE);
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
