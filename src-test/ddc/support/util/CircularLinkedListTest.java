package ddc.support.util;

import org.junit.jupiter.api.Test;

class CircularLinkedListTest {

	@Test
	void testGetLastInt() {
		CircularLinkedList<Integer> l = new CircularLinkedList<>(5);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(6);

		System.out.println("----(1) list ----");
		for (int i : l)
			System.out.println(i);

		System.out.println("----(1) last ----");
		for (int i = 0; i <= 6; i++)
			try {
				System.out.println("getLast:" + i + " > " + l.getLast(i));
			} catch (Exception e) {
				System.out.println("index:" + i + " exception: " + e.getMessage());
			}

	}

	@Test
	void testIsValidIndex() {
		CircularLinkedList<Integer> l = new CircularLinkedList<>(5);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);

		System.out.println("----(2) list ----");
		for (int i : l)
			System.out.println(i);

		System.out.println("----(2) isIndexFromLastValid ----");
		for (int i = 0; i <= 6; i++)
			System.out.println("isIndexFromLastValid:" + i + " > " + l.isIndexFromLastValid(i));

	}

}
