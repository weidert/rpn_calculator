package com.heliomug.java.rpncalc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class DisplayMenuBar extends JMenuBar implements ActionListener {
	Calculator calculator;
	
	public DisplayMenuBar(Calculator c) {
		calculator = c;
		setupMenuBar();
	}

	// constructor stuff
	private void setupMenuBar() {
		this.add(fileMenu());
		this.add(editMenu());
		// number menu
		this.add(macroMenu());
		this.add(settingsMenu());
		this.add(helpMenu());
	}

	private JMenu fileMenu() {
		JMenu menu, submenu;
		JMenuItem item;
		
		menu = makeMenu("File", KeyEvent.VK_F);
		item = new MenuItemStandardCommand("Reset Calculator", StandardCommand.RESET_ALL, KeyEvent.VK_R);
		item.setAccelerator(KeyStroke.getKeyStroke("control N"));
		menu.add(item);
		item = new MenuItemStandardCommand("Open Calculator", StandardCommand.LOAD_ALL, KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke("control O"));
		menu.add(item);
		item = new MenuItemStandardCommand("Save Calculator", StandardCommand.SAVE_ALL, KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke("control S"));
		menu.add(item);
		menu.addSeparator();
		submenu = makeMenu("Reset Special", KeyEvent.VK_E);
		submenu.add(new MenuItemStandardCommand("Reset Macros", StandardCommand.RESET_MACROS, KeyEvent.VK_M));
		submenu.add(new MenuItemStandardCommand("Reset Settings", StandardCommand.RESET_SETTINGS, KeyEvent.VK_S));
		submenu.add(new MenuItemStandardCommand("Reset Stack", StandardCommand.CLEAR_STACK, KeyEvent.VK_K));
		menu.add(submenu);
		submenu = makeMenu("Load Special", KeyEvent.VK_L);
		submenu.add(new MenuItemStandardCommand("Load Macros", StandardCommand.LOAD_MACROS, KeyEvent.VK_M));
		submenu.add(new MenuItemStandardCommand("Load Settings", StandardCommand.LOAD_SETTINGS, KeyEvent.VK_S));
		submenu.add(new MenuItemStandardCommand("Load Stack", StandardCommand.LOAD_STACK, KeyEvent.VK_K));
		menu.add(submenu);
		submenu = makeMenu("Save Special", KeyEvent.VK_V);
		submenu.add(new MenuItemStandardCommand("Save Macros", StandardCommand.SAVE_MACROS, KeyEvent.VK_M));
		submenu.add(new MenuItemStandardCommand("Save Settings", StandardCommand.SAVE_SETTINGS, KeyEvent.VK_S));
		submenu.add(new MenuItemStandardCommand("Save Stack", StandardCommand.SAVE_STACK, KeyEvent.VK_K));
		menu.add(submenu);
		menu.addSeparator();
		item = new MenuItemStandardCommand("Exit", StandardCommand.EXIT, KeyEvent.VK_X);
		item.setAccelerator(KeyStroke.getKeyStroke("control Q"));
		menu.add(item);
		return menu;
	}

	private JMenu editMenu() {
		JMenu menu = makeMenu("Edit", KeyEvent.VK_E);
		JMenuItem item; 
		item = new MenuItemStandardCommand("Undo", StandardCommand.UNDO, KeyEvent.VK_U);
		item.setAccelerator(KeyStroke.getKeyStroke("control Z"));
		menu.add(item);
		menu.addSeparator();
		menu.add(makeMenuItem("Preferences", KeyEvent.VK_P, null));
		return menu;
	}
	
	private JMenu macroMenu() {
		JMenu menu = makeMenu("Macro", KeyEvent.VK_M);
		JMenuItem item;
		menu.add(new MenuItemStandardCommand("Record Macro", StandardCommand.MACRO_START, KeyEvent.VK_R));
		menu.add(new MenuItemStandardCommand("End Macro", StandardCommand.MACRO_STOP, KeyEvent.VK_E));

		menu.addSeparator();
		
		item = new MenuItemStandardCommand("Run Current", StandardCommand.MACRO_RUN, KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke("F5"));
		menu.add(item);
		menu.add(new MenuItemStandardCommand("Name Current", StandardCommand.MACRO_NAME_START, KeyEvent.VK_N));
		menu.add(new MenuItemStandardCommand("Delete Current", StandardCommand.MACRO_DELETE, KeyEvent.VK_D));

		menu.addSeparator();
		
		menu.add(new StandardMenuItem("Show Macros", KeyEvent.VK_S) {
			{
				this.setAccelerator(KeyStroke.getKeyStroke("control M"));
			}
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setMacroVisible(true); return true; }
		});
		menu.add(new StandardMenuItem("Hide Macros", KeyEvent.VK_H) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setMacroVisible(false); return true; }
		});
		return menu;
	}
	
	private JMenu settingsMenu() {
		JMenu menu, submenu;
		menu = makeMenu("Settings", KeyEvent.VK_S);
		submenu = makeMenu("Angle Mode", KeyEvent.VK_A);
		submenu.add(new MenuItemChangeAngleMode("Degrees", KeyEvent.VK_D, CalculatorSettings.AngleMode.DEGREE));
		submenu.add(new MenuItemChangeAngleMode("Radians", KeyEvent.VK_R, CalculatorSettings.AngleMode.RADIAN));
		menu.add(submenu);
		this.add(menu);

		menu.addSeparator();
		
		submenu = makeMenu("Text Size", KeyEvent.VK_Z);
		for (int i = 12 ; i <= 40 ; i += 4) {
			submenu.add(new MenuItemTextSizeChange(i));
		}
		menu.add(submenu);

		submenu = makeMenu("Text Color", KeyEvent.VK_C);
		submenu.add(new MenuItemTextColorChange(Color.WHITE, "White"));
		submenu.add(new MenuItemTextColorChange(Color.BLACK, "Black"));
		submenu.add(makeMenuItem("Custom", "Custom Text Color", -1, null));
		menu.add(submenu);
		
		submenu = makeMenu("Background Color", KeyEvent.VK_B);
		submenu.add(new MenuItemBackgroundColorChange(new Color(0, 0, 64), "Midnight Blue"));
		submenu.add(new MenuItemBackgroundColorChange(new Color(64, 0, 64), "Plum"));
		submenu.add(new MenuItemBackgroundColorChange(new Color(64, 0, 0), "Maroon"));
		submenu.add(new MenuItemBackgroundColorChange(Color.BLACK, "Black"));
		submenu.add(new MenuItemBackgroundColorChange(Color.WHITE, "White"));
		submenu.add(makeMenuItem("Custom", "Custom Background Color", -1, null));
		menu.add(submenu);

		menu.addSeparator();

		submenu = makeMenu("Separators", KeyEvent.VK_S);
		submenu.add(new MenuItemSeparatorChange("Comma", ","));
		submenu.add(new MenuItemSeparatorChange("Period", "."));
		submenu.add(new MenuItemSeparatorChange("Space", " "));
		submenu.add(new MenuItemSeparatorChange("None", ""));
		menu.add(submenu);

		submenu = makeMenu("Decimal", KeyEvent.VK_D);
		submenu.add(new MenuItemDecimalChange("Period", "."));
		submenu.add(new MenuItemDecimalChange("Comma", ","));
		menu.add(submenu);

		menu.addSeparator();

		submenu = makeMenu("Button Panel", KeyEvent.VK_P);
		submenu.add(new StandardMenuItem("Show", KeyEvent.VK_S) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setKeyboardVisible(true); return true; }
		});
		submenu.add(new StandardMenuItem("Hide", KeyEvent.VK_H) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setKeyboardVisible(false); return true; }
		});
		menu.add(submenu);

		submenu = makeMenu("Keyboard Panel", KeyEvent.VK_K);
		submenu.add(new StandardMenuItem("Show", KeyEvent.VK_S) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setQwertyVisible(true); return true; }
		});
		submenu.add(new StandardMenuItem("Hide", KeyEvent.VK_H) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setQwertyVisible(false); return true; }
		});
		menu.add(submenu);

		submenu = makeMenu("Macro Panel", KeyEvent.VK_M);
		submenu.add(new StandardMenuItem("Show", KeyEvent.VK_S) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setMacroVisible(true); return true; }
		});
		submenu.add(new StandardMenuItem("Hide", KeyEvent.VK_H) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setMacroVisible(false); return true; }
		});
		menu.add(submenu);

		submenu = makeMenu("Commands Panel", KeyEvent.VK_C);
		submenu.add(new StandardMenuItem("Show", KeyEvent.VK_S) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setCommandsVisible(true); return true; }
		});
		submenu.add(new StandardMenuItem("Hide", KeyEvent.VK_H) {
			public boolean run(Calculator c) { c.getDisplay().getDisplaySettings().setCommandsVisible(false); return true; }
		});
		menu.add(submenu);

		return menu;
	}
	
	private JMenu helpMenu() {
		JMenu menu = makeMenu("Info", KeyEvent.VK_I);
		menu.add(makeMenuItem("What is this?", KeyEvent.VK_W, null));
		menu.add(makeMenuItem("About", KeyEvent.VK_A, null));
		return menu;
	}
	
	// aux builder helper
	private JMenu makeMenu(String name, int key) {
		JMenu toRet = new JMenu(name);
		toRet.setMnemonic(key);
		return toRet;
	}

	private JMenuItem makeMenuItem(String name, int key, KeyStroke stroke) {
		JMenuItem toRet;
		if (key != -1) {
			toRet = new JMenuItem(name, key);
		} else {
			toRet = new JMenuItem(name);
		}
		if (stroke != null) toRet.setAccelerator(stroke);
		
		toRet.addActionListener(this);
		
		return toRet;
	}

	private JMenuItem makeMenuItem(String name, String actionCommand, int key, KeyStroke stroke) {
		JMenuItem toRet;
		if (key != -1) {
			toRet = new JMenuItem(name, key);
		} else {
			toRet = new JMenuItem(name);
		}
		if (stroke != null) toRet.setAccelerator(stroke);
		toRet.setActionCommand(actionCommand);
		toRet.addActionListener(this);
		
		return toRet;
	}

	
	private abstract class StandardMenuItem extends JMenuItem implements Command {
		public StandardMenuItem(String text, int code) {
			super(text);
			this.setMnemonic(code);
			this.addActionListener(DisplayMenuBar.this);
		}

		@Override public boolean isRecordable() { return false; }
		@Override public String getAbbreviation() { return "<mnu>"; }
		@Override public String getButtonString() { return ""; }
	}
	
	private abstract class MenuItemCommand extends JMenuItem implements Command {
		public MenuItemCommand() { super();	}
		
		public MenuItemCommand(String str) { super(str); }
		
		public boolean isRecordable() { return false; }
		public String getAbbreviation() { return "<menu>"; }
		public String getButtonString() { return getName(); }
	}
	
	private class MenuItemStandardCommand extends MenuItemCommand {
		private Command command;
		
		public MenuItemStandardCommand(String text, Command command, int keyCode) {
			super(text);
			this.setMnemonic(keyCode);
			this.addActionListener(DisplayMenuBar.this);
			this.command = command;
		}

		public boolean run(Calculator c) {
			if (command == null) {
				return false; 
			} else {
				return c.executeCommand(command);
			}
		}
	}
	
	private class MenuItemChangeAngleMode extends MenuItemCommand {
		private CalculatorSettings.AngleMode angleMode;
		
		public MenuItemChangeAngleMode(String name, int keyCode, CalculatorSettings.AngleMode am) {
			this.angleMode = am;
			this.setText(name);
			this.setMnemonic(keyCode);
			this.addActionListener(DisplayMenuBar.this);
		}
		
		public boolean run(Calculator c) {
			c.getSettings().setAngleMode(angleMode);
			c.getDisplay().repaint();
			return true;
		}
	}
		
	private class MenuItemTextColorChange extends MenuItemCommand {
		Color color;
		
		public MenuItemTextColorChange(Color color, String text) {
			super(text);
			this.addActionListener(DisplayMenuBar.this);
			this.color = color;
		}
		
		public boolean run(Calculator c) {
			c.getDisplay().setTextColor(color);
			return true;
		}
	}
	
	private class MenuItemBackgroundColorChange extends MenuItemCommand {
		Color color;
		
		public MenuItemBackgroundColorChange(Color color, String text) {
			super(text);
			this.addActionListener(DisplayMenuBar.this);
			this.color = color;
		}
		
		public boolean run(Calculator c) {
			c.getDisplay().setBackgroundColor(this.color);
			return true;
		}
	}

	private class MenuItemTextSizeChange extends MenuItemCommand {
		private int size;
		
		public MenuItemTextSizeChange(int size) { 
			this.size = size; 
			this.setText("" + size);
			this.addActionListener(DisplayMenuBar.this);
		}
		
		public boolean run(Calculator c) {
			c.getDisplay().setTextSize(size);
			return true;
		}
	}
	
	private class MenuItemSeparatorChange extends MenuItemCommand {
		private String separator;
		
		public MenuItemSeparatorChange(String text, String sep) {
			super(text);
			this.addActionListener(DisplayMenuBar.this);
			this.separator = sep;
		}
		
		public boolean run(Calculator c) {
			c.getDisplay().setSeparator(separator);
			return true;
		}
	}
	
	private class MenuItemDecimalChange extends MenuItemCommand {
		private String decimal;
		
		public MenuItemDecimalChange(String text, String dec) {
			super(text);
			this.addActionListener(DisplayMenuBar.this);
			this.decimal = dec;
		}
		
		public boolean run(Calculator c) {
			c.getDisplay().getDisplaySettings().setDecimal(decimal);
			return true;
		}
	}
	
	// listener to run the command
	@Override
	public void actionPerformed(ActionEvent e) {
		Color color;
		switch(e.getActionCommand()) {
			case "About":
				calculator.getDisplay().showAboutDialog();
				break;
			case "Preferences":
				calculator.getDisplay().showPreferencesDialog();
				break;
			case "Custom Text Color":
				color = JColorChooser.showDialog(null, "Select Text Color", calculator.getDisplay().getDisplaySettings().getTextColor());
				if (color != null) {
					calculator.getDisplay().setTextColor(color);
				}
				break;
			case "Custom Background Color":
				color = JColorChooser.showDialog(null, "Select Background Color", calculator.getDisplay().getDisplaySettings().getBackgroundColor());
				if (color != null) {
					calculator.getDisplay().setBackgroundColor(color);
				}
				break;
			case "What is this?":
				calculator.getDisplay().showPopUpMessage("This is a reverse Polish notation calculator.  \nIt's also a fun project.  ", "What is this?");
				break;
			default:
				if (e.getSource() instanceof Command) {
					Command command = (Command) e.getSource();
					command.run(calculator);
				}
				break;
		}
		
	}
	
}
