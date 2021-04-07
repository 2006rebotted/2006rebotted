package com.runescape.scene.object.tile;

import com.runescape.cache.FileArchive;
import com.runescape.io.Buffer;

public final class Floor {

	public static void init(FileArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.readFile("flo.dat"));
		int cacheSize = stream.readUShort();
		if (cache == null)
			cache = new Floor[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new Floor();
			cache[j].readValues(stream);
		}

	}

	private void readValues(Buffer stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			else if (i == 1) {
				anInt390 = stream.readTriByte();
				method262(anInt390);
			} else if (i == 2)
				anInt391 = stream.readUnsignedByte();
			else if (i == 3) {
			} else if (i == 5)
				aBoolean393 = false;
			else if (i == 6)
				stream.readString();
			else if (i == 7) {
				int j = anInt394;
				int k = anInt395;
				int l = anInt396;
				int i1 = anInt397;
				int j1 = stream.readTriByte();
				method262(j1);
				anInt394 = j;
				anInt395 = k;
				anInt396 = l;
				anInt397 = i1;
				anInt398 = i1;
			} else {
				System.out.println("Error unrecognised config code: " + i);
			}
		} while (true);
	}

	private void method262(int i) {
		double d = (double) (i >> 16 & 0xff) / 256D;
		double d1 = (double) (i >> 8 & 0xff) / 256D;
		double d2 = (double) (i & 0xff) / 256D;
		double d3 = d;
		if (d1 < d3)
			d3 = d1;
		if (d2 < d3)
			d3 = d2;
		double d4 = d;
		if (d1 > d4)
			d4 = d1;
		if (d2 > d4)
			d4 = d2;
		double d5 = 0.0D;
		double d6 = 0.0D;
		double d7 = (d3 + d4) / 2D;
		if (d3 != d4) {
			if (d7 < 0.5D)
				d6 = (d4 - d3) / (d4 + d3);
			if (d7 >= 0.5D)
				d6 = (d4 - d3) / (2D - d4 - d3);
			if (d == d4)
				d5 = (d1 - d2) / (d4 - d3);
			else if (d1 == d4)
				d5 = 2D + (d2 - d) / (d4 - d3);
			else if (d2 == d4)
				d5 = 4D + (d - d1) / (d4 - d3);
		}
		d5 /= 6D;
		anInt394 = (int) (d5 * 256D);
		anInt395 = (int) (d6 * 256D);
		anInt396 = (int) (d7 * 256D);
		if (d7 > 0.5D)
			anInt398 = (int) ((1.0D - d7) * d6 * 512D);
		else
			anInt398 = (int) (d7 * d6 * 512D);
		if (anInt398 < 1)
			anInt398 = 1;
		anInt397 = (int) (d5 * (double) anInt398);
		int k = anInt394;
		int l = anInt395;
		int i1 = anInt396;
		anInt399 = method263(k, l, i1);
	}

	private int method263(int i, int j, int k) {
		if (k > 179)
			j /= 2;
		if (k > 192)
			j /= 2;
		if (k > 217)
			j /= 2;
		if (k > 243)
			j /= 2;
		return (i / 4 << 10) + (j / 32 << 7) + k / 2;
	}

	private Floor() {
		anInt391 = -1;
		aBoolean393 = true;
	}

	public static Floor cache[];
	public int anInt390;
	public int anInt391;
	public boolean aBoolean393;
	public int anInt394;
	public int anInt395;
	public int anInt396;
	public int anInt397;
	public int anInt398;
	public int anInt399;
}
