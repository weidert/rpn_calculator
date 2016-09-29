package com.heliomug.java.rpncalc;

import java.io.Serializable;

public class Stack extends Saveable {
	private static final long serialVersionUID = -6534079725491033942L;

	public static final String FILE_DESC = "RPN Stack";
	public static final String FILE_EXT = "rpnstack";

	private Node top;
	
	public Stack() {
		top = null;
	}
	
	public Stack(Node n) {
		top = n;
	}

	public Stack(Stack ms) {
		this();
		if (ms.top != null) {
			this.top = ms.top.copy();
		}
	}
	
	// copier
	public Stack copy() {
		return new Stack(this);
	}
	
	// getters
	public boolean isEmpty() { return size() == 0; }
	
	public int size() {
		Node current = top;
		int size = 0;
		while (current != null) {
			current = current.next;
			size++;
		}
		return size;
	}
	
	public Num peek() {
		return top.payload;
	}
	
	public Num deepPeek(int n) {
		if (n >= size()) {
			return null;
		} else {
			Node toRet = top;
			for (int i = 0 ; i < n ; i++) {
				toRet = toRet.next;
			}
			return toRet.payload;
		}
	}
	
	public String getFileDescription() { return FILE_DESC; }
	public String getFileExtension() { return FILE_EXT; }
	
	// setters
	public void push(Num n) {
		Node newNode = new Node(n);
		newNode.next = top;
		top = newNode;
	}

	public Num pop() {
		if (top == null) {
			return null;
		} else {
			Num toRet = top.payload;
			top = top.next;
			return toRet;
		}
	}
	
	public void cycle() {
		if (top == null) return;
		if (top.next == null) return;
		Node last = top;
		Node second = top.next;
		while (last.next != null) {
			last = last.next;
		}
		top.next = null;
		last.next = top;
		top = second;
	}
	
	public void reverseCycle() {
		if (top == null) return;
		if (top.next == null) return;
		Node last = top;
		while (last.next.next != null) {
			last = last.next;
		}
		Node newTop = last.next;
		last.next = null;
		newTop.next = top;
		top = newTop;
	}
	
	public String toString() {
		int digitSize = (int)Math.ceil(Math.log10(this.size())+1);
		String toRet = "---top---\n";
		Node current = top;
		int index = 0;
		String formatString = "%" + digitSize + "d) %s \n"; 
		while (current != null) {
			toRet += String.format(formatString, index, current.payload);
			current = current.next;
			index++;
		}
		return toRet;
	}
	
	public static Stack load() {
		Object loadedObject = FileHandler.loadObject(FILE_DESC, FILE_EXT); 
		if (loadedObject != null && loadedObject.getClass() == Stack.class) {
			return (Stack) loadedObject;
		}
		return null;
	}

	// aux class
	private class Node implements Serializable {
		private static final long serialVersionUID = -475756687271493871L;

		Num payload;
		Node next;
		
		public Node(Num n) {
			next = null;
			payload = n;
		}
		
		public Node copy() {
			if (next == null) {
				return new Node(payload.copy());
			} else {
				Node toRet = new Node(payload.copy());
				toRet.next = next.copy();
				return toRet;
			}
		}
	}	
}
