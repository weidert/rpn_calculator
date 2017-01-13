package com.heliomug.calculator.gui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.heliomug.calculator.Calculator;
import com.heliomug.calculator.Stack;

@SuppressWarnings("serial")
public class PanelStack extends JPanel {
	public PanelStack() {
		super();
		setBackground(Frame.BACKGROUND_COLOR);
		setLayout(new GridLayout(0, 1));
		
		for (int i = Frame.STACK_LINES - 1 ; i >= 0; i--) {
			int index = i;
			Supplier<String> sup = () -> {
				Calculator calc = Frame.getCalculator();
				Stack stack = calc.getStack();
				if (stack.size() <= index) {
					return String.format("%s", Frame.EMPTY_STRING);
				} else {
					if (calc.isEntryEditable() && index == 0) {
						return stack.get(index).toString();
					} else {
						return stack.get(index).toStringWithDec(Frame.DECIMAL_PLACES_TO_SHOW);
					}
				}
			};
			JLabel label = new JLabel("   ") {
				@Override
				public void paint(Graphics g) {
					setText(sup.get());
					super.paint(g);
				}
			};
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setForeground(Frame.TEXT_COLOR);
			label.setFont(Frame.TEXT_FONT);
 			add(label);
		}
	}
}
