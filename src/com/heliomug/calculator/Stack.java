package com.heliomug.calculator;

import java.io.Serializable;

public class Stack implements Serializable {
	private static final long serialVersionUID = 5562049630185402048L;

	private Node head;
	private Node tail;
	private int size;
	
	public Stack() {
		clear();
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public int size() {
		return this.size;
	}
	
	public Num peek() {
		return head.payload;
	}
	
	public Num pop() {
		if (isEmpty()) {
			return null;
		} else {
			Num num = head.payload;
			head = head.next;
			if (head == null) {
				tail = null;
			} else {
				head.prev = null;
			}
			size--;
			return num;
		}
	}
	
	public void push(Num num) {
		if (isEmpty()) {
			tail = new Node(num);
			head = tail;
		} else {
			Node n = new Node(num);
			n.next = head;
			head.prev = n;
			head = n;
		}
		
		size++;
	}
	
	public void clear() {
		head = tail = null;
		size = 0;
	}
	
	public Num get(int index) {
		if (index < size) {
			int i = 0;
			Node n = head;
			while (i < index) {
				n = n.next;
				i++;
			}
			return n.payload;
		} else {
			return null;
		}
	}

	private void swapTwoElementStack() {
		Node n = head;
		Node m = tail;
		n.prev = tail;
		n.next = null;
		m.next = head;
		m.prev = null;
		head = m;
		tail = n;
	}
	
	public void cycle() {
		if (size > 2) {
			Node n = head;
			head = head.next;
			n.next = null;
			head.prev = null;
			tail.next = n;
			n.prev = tail;
			tail = n;
		} else if (size == 2) {
			swapTwoElementStack();
		}
	}
	
	public void reverseCycle() {
		if (size > 2) {
			Node n = tail;
			tail = tail.prev;
			tail.next = null;
			n.prev = null;
			n.next = head;
			head.prev = n;
			head = n;
		} else if (size == 2) {
			swapTwoElementStack();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("stack: " + size);
		Node n = head;
		for (int i = 0 ; i < size() ; i++) {
			Num num = n.payload;
			sb.append(String.format("%3d: %s\n", i, num));
			n = n.next;
		}
		return sb.toString();
	}
	
	private class Node implements Serializable {
		private static final long serialVersionUID = -8565116723692637201L;

		Node next;
		Node prev;
		Num payload;
		
		public Node (Num num) {
			this.payload = num;
			this.next = null;
			this.prev = null;
		}
	}
}
