package ddc.support.util;

import java.util.LinkedList;

public class CircularLinkedList<E> extends LinkedList<E> {
	private static final long serialVersionUID = -6047051655066264444L;
	private int sizeLimit = 0;

	public CircularLinkedList(int sizeLimit) {
		super();
		this.sizeLimit = sizeLimit;
	}

	@Override
	public boolean add(E e) {
		if (this.size() >= sizeLimit) {
			this.removeFirst();
		}
		return super.add(e);
	}

	/**
	 * Get the item starting from last. 0 is the last. 1 is before the last. etc.
	 * getLast(0) is equals as getLast();
	 * 
	 * @param indexBeforeLast
	 * @return size - indexBeforeLast - 1 element
	 * @throws IndexOutOfBoundsException
	 */
	public E getLast(int indexBeforeLast) {
		int index = this.size() - indexBeforeLast - 1;
		return this.get(index);
	}

	public boolean isIndexFromLastValid(int indexBeforeLast) {
		int index = this.size() - indexBeforeLast - 1;
		return isIndexValid(index);
	}
	
	public boolean isIndexValid(int index) {
		return (index >= 0 && index < this.size());
	}

}
