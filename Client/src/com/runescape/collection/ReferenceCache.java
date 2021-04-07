package com.runescape.collection;
import com.runescape.sign.SignLink;

/**
 * A least-recently used cache of references, backed by a {@link HashTable} and a {@link Queue}.
 */
public final class ReferenceCache {
	
	/**
	 * The empty cacheable.
	 */
	private final Cacheable empty;
	/**
	 * The capacity of this cache.
	 */
	private final int capacity;
	/**
	 * The amount of unused slots in this cache.
	 */
	private int spaceLeft;
	/**
	 * The HashTable backing this cache.
	 */
	private final HashTable table;
	/**
	 * The queue of references, used for LRU behaviour.
	 */
	private final Queue references;

	/**
	 * Creates the ReferenceCache.
	 *
	 * @param capacity The capacity of this cache.
	 */
	public ReferenceCache(int i) {
		empty = new Cacheable();
		references = new Queue();
		capacity = i;
		spaceLeft = i;
		table = new HashTable();
	}

	/**
	 * Gets the {@link Cacheable} with the specified key.
	 * 
	 * @param key The key.
	 * @return The Cacheable.
	 */
	public Cacheable get(long key) {
		Cacheable cacheable = (Cacheable) table.get(key);
		if (cacheable != null) {
			references.insertHead(cacheable);
		}
		return cacheable;
	}

	public void put(Cacheable node, long key) {
		try {
			if (spaceLeft == 0) {
				Cacheable front = references.popTail();
				front.unlink();
				front.unlinkCacheable();
				if (front == empty) {
					front = references.popTail();
					front.unlink();
					front.unlinkCacheable();
				}
			} else {
				spaceLeft--;
			}
			table.put(node, key);
			references.insertHead(node);
			return;
		} catch (RuntimeException runtimeexception) {
			SignLink.reporterror("47547, " + node + ", " + key + ", " + (byte) 2 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}
	
	/**
	 * Clears the contents of this ReferenceCache.
	 */
	public void clear() {
		do {
			Cacheable front = references.popTail();
			if (front != null) {
				front.unlink();
				front.unlinkCacheable();
			} else {
				spaceLeft = capacity;
				return;
			}
		} while (true);
	}
}
