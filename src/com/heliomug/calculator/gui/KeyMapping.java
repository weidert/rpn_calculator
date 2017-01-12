package com.heliomug.calculator.gui;

import java.awt.event.KeyEvent;

import com.heliomug.calculator.Command;
import com.heliomug.calculator.StandardCommand;

public enum KeyMapping {
	ADD(KeyEvent.VK_ADD, StandardCommand.ADD),
	MULTITPLY(KeyEvent.VK_MULTIPLY, StandardCommand.MULT),
	SLASH(KeyEvent.VK_SLASH, StandardCommand.DIV),
	DIVIDE(KeyEvent.VK_DIVIDE, StandardCommand.DIV),
	MINUS(KeyEvent.VK_MINUS, StandardCommand.SUB),
	SUB(KeyEvent.VK_SUBTRACT, StandardCommand.SUB),
	NUM_0(KeyEvent.VK_0, StandardCommand.PUT_0, false),
	NUM_1(KeyEvent.VK_1, StandardCommand.PUT_1, false),
	NUM_2(KeyEvent.VK_2, StandardCommand.PUT_2, false),
	NUM_3(KeyEvent.VK_3, StandardCommand.PUT_3, false),
	NUM_4(KeyEvent.VK_4, StandardCommand.PUT_4, false),
	NUM_5(KeyEvent.VK_5, StandardCommand.PUT_5, false),
	NUM_6(KeyEvent.VK_6, StandardCommand.PUT_6, false),
	NUM_7(KeyEvent.VK_7, StandardCommand.PUT_7, false),
	NUM_8(KeyEvent.VK_8, StandardCommand.PUT_8, false),
	NUM_9(KeyEvent.VK_9, StandardCommand.PUT_9, false),
	BANG(KeyEvent.VK_1, StandardCommand.FACT, true),
	MOD(KeyEvent.VK_5, StandardCommand.MOD, true), 
	CARET(KeyEvent.VK_6, StandardCommand.POW, true),
	ASTERISK(KeyEvent.VK_8, StandardCommand.MULT, true),
	PLUS(KeyEvent.VK_EQUALS, StandardCommand.ADD, true),
	NUMPAD_0(KeyEvent.VK_NUMPAD0, StandardCommand.PUT_0),
	NUMPAD_1(KeyEvent.VK_NUMPAD1, StandardCommand.PUT_1),
	NUMPAD_2(KeyEvent.VK_NUMPAD2, StandardCommand.PUT_2),
	NUMPAD_3(KeyEvent.VK_NUMPAD3, StandardCommand.PUT_3),
	NUMPAD_4(KeyEvent.VK_NUMPAD4, StandardCommand.PUT_4),
	NUMPAD_5(KeyEvent.VK_NUMPAD5, StandardCommand.PUT_5),
	NUMPAD_6(KeyEvent.VK_NUMPAD6, StandardCommand.PUT_6),
	NUMPAD_7(KeyEvent.VK_NUMPAD7, StandardCommand.PUT_7),
	NUMPAD_8(KeyEvent.VK_NUMPAD8, StandardCommand.PUT_8),
	NUMPAD_9(KeyEvent.VK_NUMPAD9, StandardCommand.PUT_9),
	NUMPAD_DECIMAL(KeyEvent.VK_DECIMAL, StandardCommand.PUT_DECIMAL),
	PERIOD(KeyEvent.VK_PERIOD, StandardCommand.PUT_DECIMAL),
	BACKSPACE(KeyEvent.VK_BACK_SPACE, StandardCommand.BACKSPACE), 
	ENTER(KeyEvent.VK_ENTER, StandardCommand.ENTER, false),
	ENTERS(KeyEvent.VK_ENTER, StandardCommand.DROP, true),
	SPACE(KeyEvent.VK_SPACE, StandardCommand.ENTER, false),
	SPACES(KeyEvent.VK_SPACE, StandardCommand.DROP, true),
	
	U(KeyEvent.VK_U, StandardCommand.PUT_4, false),
	I(KeyEvent.VK_I, StandardCommand.PUT_5, false),
	O(KeyEvent.VK_O, StandardCommand.PUT_6, false),
	J(KeyEvent.VK_J, StandardCommand.PUT_1, false),
	K(KeyEvent.VK_K, StandardCommand.PUT_2, false),
	L(KeyEvent.VK_L, StandardCommand.PUT_3, false),
	M(KeyEvent.VK_M, StandardCommand.PUT_0, false),
	
	Z(KeyEvent.VK_Z, StandardCommand.COMBO, false),
	ZS(KeyEvent.VK_Z, StandardCommand.PERM, true),
	X(KeyEvent.VK_X, StandardCommand.SIN, false),
	XS(KeyEvent.VK_X, StandardCommand.ASIN, true),
	C(KeyEvent.VK_C, StandardCommand.COS, false),
	CS(KeyEvent.VK_C, StandardCommand.ACOS, true),
	V(KeyEvent.VK_V, StandardCommand.TAN, false),
	VS(KeyEvent.VK_V, StandardCommand.ATAN, true),
	B(KeyEvent.VK_B, StandardCommand.MACRO_TOGGLE, false),
	
	A(KeyEvent.VK_A, StandardCommand.DIV, false), 
	S(KeyEvent.VK_S, StandardCommand.SUB, false), 
	D(KeyEvent.VK_D, StandardCommand.MULT, false), 
	F(KeyEvent.VK_F, StandardCommand.ADD, false), 
	G(KeyEvent.VK_G, StandardCommand.MACRO_RUN, false), 
	GS(KeyEvent.VK_G, StandardCommand.STORE, true),

	Q(KeyEvent.VK_Q, StandardCommand.SQRT, false), 
	QS(KeyEvent.VK_Q, StandardCommand.ROOT, true), 
	W(KeyEvent.VK_W, StandardCommand.SWAP, false), 
	E(KeyEvent.VK_E, StandardCommand.POW, false), 
	ES(KeyEvent.VK_E, StandardCommand.SQUARE, true), 
	R(KeyEvent.VK_R, StandardCommand.RECIP, false),
	T(KeyEvent.VK_T, StandardCommand.PM, false), 
	
	DEL(KeyEvent.VK_DELETE, StandardCommand.CLEAR_ENTRY, false),
	DELS(KeyEvent.VK_DELETE, StandardCommand.CLEAR_STACK, true),
	PAGE_DOWN(KeyEvent.VK_PAGE_DOWN, StandardCommand.CYCLE, false),
	PAGE_UP(KeyEvent.VK_PAGE_UP, StandardCommand.REVERSE_CYCLE, false),
	SLASHS(KeyEvent.VK_SLASH, StandardCommand.RAND, true),
	
	NULL(0, null);
	
	private int key;
	private Command command;
	private boolean withShift;
	private boolean withoutShift;
	
	private KeyMapping(int key, Command com) {
		this.key = key;
		this.command = com;
		this.withShift = true;
		this.withoutShift = true;
	}

	private KeyMapping(int key, Command com, boolean shift) {
		this.key = key;
		this.command = com;
		this.withShift = shift;
		this.withoutShift = !shift;
	}

	public int getKey() {
		return key;
	}

	public Command getCommand() {
		return command;
	}

	public boolean isWithShift() {
		return withShift;
	}

	public boolean isWithoutShift() {
		return withoutShift;
	}
}
