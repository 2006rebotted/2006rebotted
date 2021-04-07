package com.runescape.collection;

import com.runescape.sign.SignLink;

final class HashTable {

	private final int bucketCount;
	private final Linkable[] buckets;

	/**
	 * Creates the HashTable with the specified size.
	 */
	public HashTable() {
		int size = 1024;// was parameter
		bucketCount = size;
		buckets = new Linkable[size];
		for (int index = 0; index < size; index++) {
			Linkable node = buckets[index] = new Linkable();
			node.previous = node;
			node.next = node;
		}
	}

	/**
	 * Gets the {@link Linkable} with the specified {@code key} from this
	 * HashTable.
	 * 
	 * @param key
	 *            The key.
	 * @return The Linkable, or {@code null} if this HashTable does not contain
	 *         an associated for the specified key.
	 */
	public Linkable get(long key) {
		Linkable linkable = buckets[(int) (key & (long) (bucketCount - 1))];
		for (Linkable next = linkable.previous; next != linkable; next = next.previous)
			if (next.key == key)
				return next;

		return null;
	}

	/**
	 * Associates the specified {@link Linkable} with the specified {@code key}.
	 * 
	 * @param key
	 *            The key.
	 * @param linkable
	 *            The Linkable.
	 */
	public void put(Linkable linkable, long key) {
		try {
			if (linkable.next != null)
				linkable.unlink();
			Linkable current = buckets[(int) (key & (long) (bucketCount - 1))];
			linkable.next = current.next;
			linkable.previous = current;
			linkable.next.previous = linkable;
			linkable.previous.next = linkable;
			linkable.key = key;
			return;
		} catch (RuntimeException runtimeexception) {
			SignLink.reporterror("91499, " + linkable + ", " + key + ", "
					+ (byte) 7 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}
}
