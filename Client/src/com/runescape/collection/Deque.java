package com.runescape.collection;

public final class Deque {
	
	private final Linkable head;
	private Linkable current;

	public Deque() {
		head = new Linkable();
		head.previous = head;
		head.next = head;
	}

	public void insertHead(Linkable linkable) {
		if (linkable.next != null)
			linkable.unlink();
		linkable.next = head.next;
		linkable.previous = head;
		linkable.next.previous = linkable;
		linkable.previous.next = linkable;
	}

	public void insertTail(Linkable linkable) {
		if (linkable.next != null)
			linkable.unlink();
		linkable.next = head;
		linkable.previous = head.previous;
		linkable.next.previous = linkable;
		linkable.previous.next = linkable;
	}

	public Linkable popHead() {
		Linkable node = head.previous;
		if (node == head) {
			return null;
		} else {
			node.unlink();
			return node;
		}
	}

	public Linkable reverseGetFirst() {
		Linkable node = head.previous;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.previous;
			return node;
		}
	}

	public Linkable getFirst() {
		Linkable node = head.next;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}

	public Linkable reverseGetNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.previous;
			return node;
		}
	}

	public Linkable getNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		}
		current = node.next;
		return node;
	}

	public void clear() {
		if (head.previous == head)
			return;
		do {
			Linkable node = head.previous;
			if (node == head)
				return;
			node.unlink();
		} while (true);
	}
}
