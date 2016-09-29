package com.heliomug.java.rpncalc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class Display extends JFrame {
	private static final String NULL_LABEL_TEXT = "---";
	private static final String LABEL_FORMATTING_STRING = "%30s";

	private static final Font KEYBOARD_MAIN_LETTER_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 24);
	private static final Font KEYBOARD_COMMAND_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	
	private static final Color BORDER_COLOR = Color.WHITE;
	
	public static final String MACRO_NOT_YET = "";
	public static final String MACRO_REC_OFF_TEXT = "mac: ";
	public static final String MACRO_ON_TEXT = "[rec]"; 
	public static final String MACRO_ADD_COMMAND_TEXT = "[rec]+"; 
	public static final String MACRO_NAME_INPUT_READY_TEXT = "[set name]";

	public static final String MACROS_LOADED = "[macros loaded]";
	public static final String MACROS_SAVED = "[macros saved]";
	public static final String MACRO_REMOVED = "[mac del: %s]";
	public static final String STACK_LOADED = "[stack loaded]";
	public static final String STACK_SAVED = "[stack saved]";
	public static final String CONFIG_LOADED = "[config loaded]";
	public static final String CONFIG_SAVED = "[config saved]";
	public static final String CALCULATOR_LOADED = "[calculator loaded]";
	public static final String CALCULATOR_SAVED = "[calculator saved]";
	public static final String LOAD_FAILED = "[load failed]";

	public static final String TEXT_INPUT_OFF = "";
	public static final String TEXT_INPUT_CANCELLED = "";
	public static final String TEXT_INPUT_READY = "[command?]";
	public static final String TEXT_INPUT_NOT_FOUND = "No such: ";
	public static final String TEXT_INPUT_PARTIAL_NOT_FOUND = "No possible commands start with \"%s\"";	

	public static final String FAILURE_TEXT = "!";

	private enum InitialLocation {
		RIGHT_UPPER, 
		UNDER_LEFT,
		CENTERED;
	}
	
	private Calculator calculator;
	
	private String textMacro;
	private String textMessage;
	private String textInputMessage;
	private String textInputDisplay;
	
	private DisplaySettings displaySettings;
	
	private JPanel keyboardPanel, qwertyPanel;
	private JScrollPane commandPane, macroPane;
	
	
	public Display(Calculator calculator) {
		super("Heliomug RPN Calculator");
		
		this.calculator = calculator;
		displaySettings = new DisplaySettings();

		textMacro = textMessage = textInputMessage = textInputDisplay = "";
		
		setIcons(this);

		this.setLocationByPlatform(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			@Override 
			public void windowClosing(WindowEvent e) { 
				calculator.quit(); 
			}
		}); 

		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.addKeyListener(new KeyAdapter() {
			@Override 
			public void keyPressed(KeyEvent e) { 
				calculator.handleKey(e); 
			}
		});

		this.setJMenuBar(new DisplayMenuBar(calculator));

		setupGUI();
		
		this.pack();

		repaint();
	}

	
	private void setupGUI() {

		JPanel mainPanel = new CalcPanel();
		
		mainPanel.setLayout(new BorderLayout());

		JPanel panel, subpanel, subsubpanel;
		JLabel label;

		// calculator panel except info
		panel = new CalcPanel();
		panel.setLayout(new BorderLayout());

		
			// stack panel
			subpanel = new CalcPanel(); 
			
			int labelCount = displaySettings.getNumLabels();
			subpanel.setLayout(new GridLayout(labelCount, 1));
			subpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
			
			for (int i = 0 ; i < labelCount ; i++) {
				subpanel.add(new StackLabel(labelCount - i - 1));
			}

			
		panel.add(subpanel, BorderLayout.NORTH);
		
		
			// message, etc panel
			subpanel = new CalcPanel();
			subpanel.setOpaque(false);
			subpanel.setLayout(new GridLayout(0, 1));
			
				// for angle mode, macro info, message
				subsubpanel = new CalcPanel();
				subsubpanel.setLayout(new BorderLayout());
					
					// degree mode
					label = new CalcLabel(calculator.getSettings().getAngleMode().toString()) {
						public void paint(Graphics g) {
							this.setText(Display.this.calculator.getSettings().getAngleMode().toString());
							super.paint(g);
						}
					};
					label.setHorizontalAlignment(JLabel.LEFT);

				subsubpanel.add(label, BorderLayout.WEST);

					// macro status
					label = new CalcLabel(MACRO_NOT_YET) {
						public void paint(Graphics g) {
							this.setText(Display.this.textMacro);
							super.paint(g);
						}
					};
					label.setHorizontalAlignment(JLabel.LEFT);

				subsubpanel.add(label, BorderLayout.CENTER);
		
					// message
					label = new CalcLabel("MESSAGE") {
						public void paint(Graphics g) {
							this.setText(Display.this.textMessage);
							super.paint(g);
						}
					};
				subsubpanel.add(label, BorderLayout.EAST);
		
			subpanel.add(subsubpanel);
		
				// for text input message, text input
				subsubpanel = new CalcPanel();
				subsubpanel.setOpaque(false);
				subsubpanel.setLayout(new BorderLayout());

					// text input message
					label = new CalcLabel(TEXT_INPUT_OFF) {
						public void paint(Graphics g) {
							this.setBackground(Color.RED);
							this.setText(Display.this.textInputMessage);
							super.paint(g);
						}
					};
		
				subsubpanel.add(label, BorderLayout.WEST);

					// text input display
					label = new CalcLabel() {
						public void paint(Graphics g) {
							this.setText(Display.this.textInputDisplay);
							super.paint(g);
						}
					};

				subsubpanel.add(label, BorderLayout.EAST);

			subpanel.add(subsubpanel);

		panel.add(subpanel, BorderLayout.CENTER);
		
		// keyboard panel
		this.keyboardPanel = new ButtonPanel(calculator); 
		keyboardPanel.setVisible(false);
		panel.add(keyboardPanel, BorderLayout.SOUTH);
		
		mainPanel.add(panel, BorderLayout.CENTER);
		
		
		qwertyPanel = new QwertyPanel();
		qwertyPanel.setVisible(false);
		mainPanel.add(qwertyPanel, BorderLayout.SOUTH);
		
		
		commandPane = new JScrollPane(new CommandPanel());
		commandPane.setPreferredSize(new Dimension(100, 10));
		mainPanel.add(commandPane, BorderLayout.WEST);
		
		macroPane = new JScrollPane(new MacroPanel());
		macroPane.setPreferredSize(new Dimension(100, 10));
		mainPanel.add(macroPane, BorderLayout.EAST);
		
		
		this.add(mainPanel);
	}

	private class CalcLabel extends JLabel {
		public CalcLabel() {
			this(" ");
		}
		
		public CalcLabel(String text) {
			super(text.length() == 0 ? " " : text);
			this.setForeground(displaySettings.getTextColor());
			this.setFont(displaySettings.getTextFont());
			this.setHorizontalAlignment(JLabel.RIGHT);
		}

		@Override
		public void setText(String str) {
			super.setText(str.equals("") ? " " : str);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			this.setBackground(displaySettings.getBackgroundColor());
			this.setForeground(displaySettings.getTextColor());
			this.setFont(displaySettings.getTextFont());
			super.paintComponent(g);
		}
	}

	private class StackLabel extends CalcLabel {
		private int index;
		
		public StackLabel(int i) {
			super();
			this.index = i;
		}
		
		@Override
		public void paint(Graphics g) {
			if (index < calculator.getStack().size()) {
				String num = calculator.getStack().deepPeek(index).getString(displaySettings.getSeparator(), displaySettings.getDecimal());
				setText(String.format(LABEL_FORMATTING_STRING, num));
			} else {
				setText(String.format(LABEL_FORMATTING_STRING, NULL_LABEL_TEXT));
			}
			super.paint(g);
		}
	}
	
	private class CalcPanel extends JPanel {
		@Override
		public void paint(Graphics g) {
			this.setBackground(displaySettings.getBackgroundColor());
			super.paint(g);
		}
	}
	
	// settings stuff
	public DisplaySettings getDisplaySettings() { return displaySettings; }
	public void setDisplaySettings(DisplaySettings settings) { displaySettings = settings; }
	
	public void setBackgroundColor(Color c) { displaySettings.setBackgroundColor(c); repaint(); }
	public void setTextColor(Color c) { displaySettings.setTextColor(c); repaint(); }
	public void setTextSize(int size) { displaySettings.setTextSize(size); repaint(); }
	public void setSeparator(String str) { displaySettings.setSeparator(str); repaint(); }
	
	// message stuff
	public void setMessage(String str) { textMessage = str; repaint(); }
	public void setTextInputMessage(String str) { this.textInputMessage = str; repaint(); }
	public void setTextInputLabel(String str) { this.textInputDisplay = str; repaint(); }
	public void setMacroText(String str) { this.textMacro = str; repaint(); }
	
	public void notifyFailure() { setMessage(FAILURE_TEXT); }
	private String getMacroRecOffText() {
		if (calculator.getActiveMacro() == null) {
			return ""; 
		} else if (calculator.getActiveMacro().getName() == null) {
			return MACRO_REC_OFF_TEXT + "[no name]";
		} else {
			return MACRO_REC_OFF_TEXT + calculator.getActiveMacro().getName();
		}
	}
	public void setMacroDisplayNoRecord() { setMacroText(getMacroRecOffText()); }
	public void setMacroDisplayAddCommand(Command command) { setMacroText(MACRO_ADD_COMMAND_TEXT + command);}
	
	@Override
	public void paint(Graphics g) {
		if (!calculator.isMacroRecording()) setMacroDisplayNoRecord();
		setTextInputLabel(calculator.getTextInputString());
		keyboardPanel.setVisible(displaySettings.isKeyboardVisible());
		qwertyPanel.setVisible(displaySettings.isQwertyVisible());
		macroPane.setVisible(displaySettings.isMacroVisible());
		commandPane.setVisible(displaySettings.isCommandsVisible());
		pack();
		super.paint(g);
	}

	
	// messages
	public void showErrorPopup(String message) {
		JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.OK_OPTION);
		notifyFailure();
	}
	public void showNoPossibleCommandError(String partialName) {
		JOptionPane.showMessageDialog(this, String.format(TEXT_INPUT_PARTIAL_NOT_FOUND, partialName), "No such command!", JOptionPane.ERROR_MESSAGE);
	}
	public void showClarificationMessage(String toDisplay) { 
		JOptionPane.showMessageDialog(this, toDisplay, "Please Clarify", JOptionPane.INFORMATION_MESSAGE);	
	}
	public void showPopUpMessage(String message) { 
		JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE); 
	}
	public void showPopUpMessage(String message, String title) { 
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE); 
	}
	public void showNoMacrosAvailableDialog() {
		JOptionPane.showMessageDialog(this, "No macros currently available.", "No Macros!", JOptionPane.OK_OPTION);
	}
	
	private static void setIcons(Window window) {
		try {
			List<Image> icons = new ArrayList<Image>();
			icons.add(new ImageIcon(window.getClass().getResource("heliomug16.png")).getImage());
			icons.add(new ImageIcon(window.getClass().getResource("heliomug32.png")).getImage());
			icons.add(new ImageIcon(window.getClass().getResource("heliomug48.png")).getImage());
			icons.add(new ImageIcon(window.getClass().getResource("heliomug64.png")).getImage());
			icons.add(new ImageIcon(window.getClass().getResource("heliomug96.png")).getImage());
			icons.add(new ImageIcon(window.getClass().getResource("heliomug128.png")).getImage());
			window.setIconImages(icons);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("no icon");
		}
	}

	
	private class QwertyPanel extends JPanel {
		private KeyMap map;

		private int[][] keyArrangement = keySetup();

		private int[][] keySetup() {
			int[][] keys = new int[3][];
			keys[0] = new int[] {KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P};
			keys[1] = new int[] {KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_SEMICOLON};
			keys[2] = new int[] {KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD, KeyEvent.VK_SLASH};
			return keys;
		}
		
		public QwertyPanel() {
			super();
			map = Display.this.calculator.getKeyMap();
			this.setLayout(new GridLayout(keyArrangement.length, keyArrangement[0].length));
			for (int i = 0 ; i < keyArrangement.length ; i++) {
				for (int j = 0 ; j < keyArrangement[i].length ; j++) {
					this.add(makeKeyPanel(keyArrangement[i][j]));
				}
			}
		}
		
		@Override
		public void paint(Graphics g) {
			this.setVisible(Display.this.displaySettings.isQwertyVisible());
			super.paint(g);
		}
		
		public String getKeyChar(int keyCode) { return String.valueOf((char)(keyCode)); }
		
		private JPanel makeKeyPanel(int keyCode) {
			JPanel toRet = new JPanel();
			toRet.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			
			int numberOfCommands = map.getNumberOfCommands(keyCode);
			
			JLabel letterLabel = new JLabel(getKeyChar(keyCode)); 
			letterLabel.setFont(KEYBOARD_MAIN_LETTER_FONT);
			toRet.add(letterLabel);
			
			if (numberOfCommands > 0) {

				JPanel rightPanel = new JPanel();
				rightPanel.setOpaque(false);
				rightPanel.setLayout(new GridLayout(0, 1));
				
				JLabel label;
				
				if (map.getCommand(keyCode, false) != null) {
					label = new JLabel(map.getCommandString(keyCode, false));
					label.setForeground(Color.BLUE);
					label.setFont(KEYBOARD_COMMAND_FONT);
					rightPanel.add(label);
				}
				
				if (map.getCommand(keyCode, true ) != null) {
					label = new JLabel(map.getCommandString(keyCode, true));
					label.setFont(KEYBOARD_COMMAND_FONT);
					label.setForeground(new Color(0, 100, 0));
					rightPanel.add(label);
				}
					
				toRet.add(rightPanel);
			}		
				
			return toRet;
		}
	}

	private class MacroPanel extends JPanel {
		public MacroPanel() {
			super();
			setupGUI();
		}
		
		private void setupGUI() {
			this.setLayout(new GridLayout(0, 1));
			for (int i = 0 ; i < 100 ; i++) {
				this.add(new JLabel(String.valueOf(i)));
			}
		}
		
		/*
		public void paint(Graphics g) {
			this.setLayout(new GridLayout(0, 1));
			for (Macro mac : calculator.getMacroList()) {
				String text = mac.getName();
				this.add(new JLabel(text) { 
					{
						this.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent e) { calculator.executeCommand(mac); }
						});
					} 
				});
			}
			super.paint(g);
		}
		*/
	}
	
	private class CommandPanel extends JPanel {
		public CommandPanel() {
			super();
			
			setupGUI();
		}
		
		private void setupGUI() {
			this.setLayout(new GridLayout(0, 1));
			for (Command cmd : StandardCommand.values()) {
				String text = cmd.getName();
				if (cmd.isRecordable() && text != null && text.length() > 0) {
					this.add(new JLabel(text) { {
						this.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent e) { calculator.executeCommand(cmd); }
						});
					} });
				}
			}
		}
	}
	
	public void showPreferencesDialog() { 
		new PreferencesDialog().showAndGetFocus(); 
	}
	public void showAboutDialog() { 
		new AboutDialog().showAndGetFocus();
	}
	
	// standard dialog class	
	private abstract class StandardDialog extends JDialog {
		private InitialLocation relativeLocation;
		
		public StandardDialog(Window parentWindow, String title, InitialLocation relativeLocation) {
			super(parentWindow);
			this.relativeLocation = relativeLocation;
			setTitle(title);
			pack();
			setResizable(false);		
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
			//setIcons(this);
			this.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) { 
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						Display.this.requestFocus();
					} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						dispose();
					}
				}
			});
		}
		
		public void showAndGetFocus() {
			this.update();
			this.setVisible(true);
			this.requestFocus();
		}
		
		abstract void update();
		
		private void setLocationRight() {
			setLocationRelativeTo(this.getParent());
			int newX = this.getLocation().x + this.getParent().getWidth()/2 + this.getWidth()/2;
			int newY = this.getLocation().y - this.getParent().getHeight()/2 + this.getHeight()/2;
			setLocation(newX, newY);
		}

		private void setLocationUnder() {
			setLocationRelativeTo(this.getParent());
			int newX = this.getLocation().x - this.getParent().getWidth()/2 + this.getWidth()/2;
			int newY = this.getLocation().y + this.getParent().getHeight()/2 + this.getHeight()/2;
			setLocation(newX, newY);
		}
		
		public void setPlace() {
			if (relativeLocation == InitialLocation.RIGHT_UPPER) setLocationRight();
			if (relativeLocation == InitialLocation.UNDER_LEFT) setLocationUnder();
			if (relativeLocation == InitialLocation.CENTERED) this.setLocationRelativeTo(this.getParent());
			pack();
		}
	}
	
	private class PreferencesDialog extends StandardDialog implements ItemListener {
		private JCheckBox continueFromExitBox;
		private Preferences prefs;

		public PreferencesDialog() {
			super(Display.this.calculator.getDisplay(), "Preferences", InitialLocation.RIGHT_UPPER); 
			JPanel panel = new JPanel();
			this.prefs = Display.this.calculator.getPreferences();
			continueFromExitBox = new JCheckBox("Save calculator on exit and reload on open");
			continueFromExitBox.setSelected(prefs.getContinueFromExit());
			continueFromExitBox.addItemListener(this);
			panel.add(continueFromExitBox);
			add(panel);
			setPlace();
			setVisible(true);
		}

		public void update() {}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getItemSelectable() == continueFromExitBox) {
				boolean b = continueFromExitBox.isSelected();
				prefs.setContinueFromExit(b);
			}
		}
	}

	private class AboutDialog extends StandardDialog {
		public void update() {}

		public AboutDialog() {
			super(Display.this.calculator.getDisplay(), "About", InitialLocation.CENTERED);
			JPanel panel = new JPanel();
			panel.add(new JLabel("by Craig Weidert 2015-2016"));
			add(panel);
			setPlace();
			setVisible(true);
		}
	}
	
}