package com.heliomug.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public enum StandardCommand implements Command {
	ADD("add", "< + >", 2, (Stack stack) -> {
		stack.push(stack.pop().add(stack.pop()));
	}, "plus"),
	MULT("mult", "< * >", 2, (Stack stack) -> {
		stack.push(stack.pop().mult(stack.pop()));
	}, "multiply", "times"),
	DIV("div", "< / >", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.div(n));
	}, "divide", "over"),
	SUB("sub", "< - >", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.sub(n));
	}, "subtract", "minus"),
	MOD("mod", "< % >", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.mod(n));
	}, "modulus", "%"), 

	FACT("fact", "< ! >", 1, (Stack stack) -> {
		stack.push(stack.pop().fact());
	}, "factorial", "!"),
	COMBO("combo", "<yCx>", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.combo(n));
	}, "combination", "binom"), 
	PERM("perm", "<yPx>", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.perm(n));
	}, "permutation"), 

	RAND("rand", "<rnd>", 0, (Stack stack) -> {
		stack.push(new NumDouble(Math.random()));
	}),
	LN("ln", "<ln >", true, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty() || stack.peek().isNegative() || stack.peek().isZero()) {
			return false;
		} else {
			stack.push(stack.pop().ln());
			calc.setEntryEditable(false);
			return true;
		}
	}), 
	EXP("exp", "<exp>", 1, (Stack stack) -> {
		stack.push(stack.pop().exp());
	}), 
	LOG("log", "<log>", true, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty() || stack.peek().isNegative() || stack.peek().isZero()) {
			return false;
		} else {
			stack.push(stack.pop().ln().div(new NumDouble(10).ln()));
			calc.setEntryEditable(false);
			return true;
		}
	}), 
	EXP10("T^x", "<T^x>", 1, (Stack stack) -> {
		stack.push(new NumDouble(10).pow(stack.pop()));
	}), 
	LG("lg", "<lg>", true, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty() || stack.peek().isNegative() || stack.peek().isZero()) {
			return false;
		} else {
			stack.push(stack.pop().ln().div(new NumDouble(2).ln()));
			calc.setEntryEditable(false);
			return true;
		}
	}), 
	EXP2("2^x", "<2^x>", 1, (Stack stack) -> {
		stack.push(new NumDouble(2).pow(stack.pop()));
	}), 
	POW("pow", "<y^x>", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.pow(n));
	}, "power", "exponent", "^"),
	SQRT("sqrt", "< \u221a >", true, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty() || stack.peek().isNegative()) {
			return false;
		} else {
			stack.push(stack.pop().sqrt());
			calc.setEntryEditable(false);
			return true;
		}
	}), 
	SQUARE("sqr", "< \u00b2 >", 1, (Stack stack) -> {
		Num n = stack.pop();
		stack.push(n.mult(n));
	}), 
	PI("pi", "< \u03c0 >", 0, (Stack stack) -> {
		stack.push(new NumDouble(Math.PI));
	}), 
	ROOT("root", "<x\u221ay>", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(m.root(n));
	}), 
	RECIP("recip", "<1/x>", 1, (Stack stack) -> {
		stack.push(stack.pop().recip());
	}, "flip", "reciprocal"),
	PM("pm", "<+/->", 1, (Stack stack) -> {
		stack.push(stack.pop().pm());
	}),

	SIN("sin", "<sin>", true, new TrigFxn((Num num) -> num.sin()), "sine"),
	COS("cos", "<cos>", true, new TrigFxn((Num num) -> num.cos()), "cosine"),
	TAN("tan", "<tan>", true, new TrigFxn((Num num) -> num.tan()), "tangent"),
	ASIN("asin", "<asn>", true, 
			new InverseTrigFxn((Num num) -> num.asin(), -1, 1)),
	ACOS("acos", "<acs>", true, 
			new InverseTrigFxn((Num num) -> num.acos(), -1, 1)),
	ATAN("atan", "<atn>", true, 
			new InverseTrigFxn((Num num) -> num.atan(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)),

	PUT_0(0), 
	PUT_1(1), 
	PUT_2(2), 
	PUT_3(3), 
	PUT_4(4), 
	PUT_5(5), 
	PUT_6(6), 
	PUT_7(7), 
	PUT_8(8), 
	PUT_9(9), 
	PUT_DECIMAL(".", "< . >", true, (Calculator calc) -> {
		if (calc.isEntryEditable()) {
			Stack stack = calc.getStack();
			if (stack.isEmpty()) {
				stack.push(new NumDouble());
			}
			stack.push(stack.pop().addDecimal());
			calc.setEntryEditable(true);
			calc.setEntryClears(false);
			return true;
		} else {
			return false;
		}
	}),
	BACKSPACE("\u2190", "< \u2190 >", true, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty()) {
			return false;
		} else {
			if (calc.isEntryEditable()) {
				stack.push(stack.pop().backspace());
			} else {
				stack.pop();
				stack.push(new NumDouble());
			}
			return true;
		}
	}, "backspace"),
	SEAL("---", "<--->", false, (Calculator calc) -> {
		calc.setEntryEditable(false);
		return true;
	}),
	SWAP("swap", "< \u2194 >", 2, (Stack stack) -> {
		Num n = stack.pop();
		Num m = stack.pop();
		stack.push(n);
		stack.push(m);
	}), 
	CYCLE("cycle", "<cyc>", 0, (Stack stack) -> {
		stack.cycle();
	}),
	REVERSE_CYCLE("reverse_cycle", "<rcy>", 0, (Stack stack) -> {
		stack.reverseCycle();
	}),
	ENTER("ent", "<ent>", true, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty()) {
			stack.push(new NumDouble());
		} else {
			stack.push(stack.peek().copy());
		}
		calc.setEntryEditable(false);
		calc.setEntryClears(true);
		return true;
	}, "enter"),
	DROP("drp", "<drp>", 1, (Stack stack) -> {
		stack.pop();
	}, "enter"),
	CLEAR_ENTRY("ce", "<ce >", 0, (Stack stack) -> {
		if (!stack.isEmpty()) {
			stack.pop();
			stack.push(new NumDouble());
		}
	}),
	CLEAR_STACK("cls", "<cls>", 0, (Stack stack) -> {
		stack.clear();
	}), 

	STORE("sto", "<sto>", false, (Calculator calc) -> {
		Stack stack = calc.getStack();
		if (stack.isEmpty()) {
			return false;
		} else {
			calc.setCurrentMacro(new Macro(stack.peek().getDouble()));
			return true;
		}
	}), 
	MACRO_START("macroStart", "<mc\u2713>", false, (Calculator calc) -> {
		if (calc.isRecording()) {			calc.setEntryClears(true);

			return false;
		} else {
			calc.startMacro();
			return true;
		}
	}),
	MACRO_STOP("macroStop", "<mc\u2715>", false, (Calculator calc) -> {
		if (calc.isRecording()) {
			calc.stopMacro();
			return true;
		} else {
			return false;
		}
	}),
	MACRO_RUN("macroRun", "<run>", true, (Calculator calc) -> {
		if (calc.getCurrentMacro() != null) {
			calc.runMacro();
			return true;
		} else {
			return false;
		}
	}),
	MACRO_TOGGLE("macroToggle", "<s/s>", false, (Calculator calc) -> {
		calc.apply(StandardCommand.SEAL);
		if (calc.isRecording()) {
			calc.stopMacro();
		} else {
			calc.startMacro();
		}
		return true;
	}), 
	DRG("drg", "<drg>", true, (Calculator calc) -> {
		calc.setNextAngleMode();
		return true;
	}),
	EXIT("exit", "<xit>", 0, (Stack stack) -> System.exit(0), "quit", "q");
	
	public static Command getCommand(String string) {
		for (Command command : StandardCommand.values()) {
			if (command.hasName(string)) {
				return command;
			}
		}
		return null;
	}
	
	public static List<Command> getCandidates(String string) {
		List<Command> cands = new ArrayList<>();
		for (Command command : StandardCommand.values()) {
			if (command.hasPrefix(string)) {
				cands.add(command);
			}
		}
		return cands;
	}
	
	private List<String> names;
	private String abbrev;
	private boolean isRecordable;
	private Function<Calculator, Boolean> fxn;
	
	private StandardCommand(String primaryName, 
			String abbrev, 
			boolean isRecordable, 
			Function<Calculator, Boolean> fxn, 
			String... additionalNames) {
		this.names = new ArrayList<>();
		this.names.add(primaryName);
		this.abbrev = abbrev;
		this.isRecordable = isRecordable;
		this.fxn = fxn;
		for (String name : additionalNames) {
			names.add(name);
		}
	}
	
	private StandardCommand(String primaryName, 
			String abbrev, 
			int reqSize, 
			Consumer<Stack> stackCommand, 
			String... additionalNames) {
		this(
				primaryName, 
				abbrev, 
				true,
				(Calculator calc) -> {
					Stack stack = calc.getStack();
					if (stack.size() >= reqSize) {
						stackCommand.accept(stack);
						calc.setEntryEditable(false);
						calc.setEntryClears(false);
						return true;
					} else {
						return false;
					}
				},
				additionalNames
		);
	}

	// digit insertion
	private StandardCommand(int digit) {
		this(
				String.format("%d", digit),
				String.format("< %d >", digit),
				true,
				(Calculator calc) -> {
					Stack stack = calc.getStack();
					if (stack.isEmpty()) {
						stack.push(new NumDouble(digit));
					} else if (calc.isEntryClears()) {
						stack.pop();
						stack.push(new NumDouble(digit));
					} else if (!calc.isEntryEditable()) {
						stack.push(new NumDouble(digit));
					} else {
						stack.push(stack.pop().addDigit(digit));
					}
					calc.setEntryEditable(true);
					calc.setEntryClears(false);
					return true;
				}
		);
	}
	
	public String getName() { return names.get(0); }
	public String getAbbrev() { return abbrev; }
	
	public boolean hasPrefix(String prefix) { 
		for (String name : names) {
			if (name.startsWith(prefix)) { 
				return true;
			}
		}
		return false;
	}
	public boolean hasName(String name) { return names.contains(name); }
	
	public boolean apply(Calculator calc) {
		return fxn.apply(calc);
	}
	
	@Override
	public boolean isMacro() {
		return false;
	}
	
	@Override
	public boolean isRecordable() {
		return isRecordable;
	}
	
	@Override
	public String toString() {
		return getAbbrev();
	}
	
	
	private static class TrigFxn implements Function<Calculator, Boolean> {
		private Function<Num, Num> trigFxn;
		
		public TrigFxn(Function<Num, Num> trigFxn) {
			this.trigFxn = trigFxn;
		}
		
		public Boolean apply(Calculator calc) {
			Stack stack = calc.getStack();
			if (stack.isEmpty()) {
				return false;
			} else {
				stack.push(trigFxn.apply(calc.toRadians(stack.pop())));
				calc.setEntryClears(false);
				calc.setEntryEditable(false);
				return true;
			}
		}
	}
	
	private static class InverseTrigFxn implements Function<Calculator, Boolean> {
		private Function<Num, Num> aTrig;
		private double upper;
		private double lower;
		
		public InverseTrigFxn(Function<Num, Num> fxn, double lowerLimit, double upperLimit) {
			aTrig = fxn;
			upper = upperLimit;
			lower = lowerLimit;
			
		}
		
		public Boolean apply(Calculator calc) {
			Stack stack = calc.getStack();
			if (stack.isEmpty()) {
				return false;
			} else {
				if (stack.peek().greaterThan(upper) || stack.peek().lessThan(lower)) {
					return false;
				}
				stack.push(calc.fromRadians(aTrig.apply(stack.pop())));
				calc.setEntryClears(false);
				calc.setEntryEditable(false);
				return true;
			}
		}
	}
}
