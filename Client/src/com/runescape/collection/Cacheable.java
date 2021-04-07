package com.runescape.collection;

public class Cacheable extends Linkable {

	public Cacheable nextCacheable;
	public Cacheable previousCacheable;

	public final void unlinkCacheable() {
		if (previousCacheable == null) {
		} else {
			previousCacheable.nextCacheable = nextCacheable;
			nextCacheable.previousCacheable = previousCacheable;
			nextCacheable = null;
			previousCacheable = null;
		}
	}
}
