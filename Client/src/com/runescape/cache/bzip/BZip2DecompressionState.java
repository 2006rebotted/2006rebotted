package com.runescape.cache.bzip;

final class BZip2DecompressionState {
	
	byte compressed[];
	int nextIn;
	int decompressedLength;
	int totalInLo32;
	int totalInHi32;
	byte decompressed[];
	int nextOut;
	int length;
	int totalOutLo32;
	int totalOutHigh32;
	byte aByte573;
	int anInt574;
	boolean aBoolean575;
	int bsBuff;
	int bsLive;
	int anInt578;
	int currentBlock;
	int randomised;
	int anInt581;
	int anInt582;
	final int[] unzftab;
	int anInt584;
	final int[] cftab;
	public static int tt[];
	int nInUse;
	final boolean[] inUse;
	final boolean[] inUse16;
	final byte[] seqToUnseq;
	final byte[] mtfa;
	final int[] mtfbase;
	final byte[] selector;
	final byte[] selectorMtf;
	final byte[][] len;
	final int[][] limit;
	final int[][] base;
	final int[][] perm;
	final int[] minLens;
	int anInt601;

	BZip2DecompressionState() {
		unzftab = new int[256];
		cftab = new int[257];
		inUse = new boolean[256];
		inUse16 = new boolean[16];
		seqToUnseq = new byte[256];
		mtfa = new byte[4096];
		mtfbase = new int[16];
		selector = new byte[18002];
		selectorMtf = new byte[18002];
		len = new byte[6][258];
		limit = new int[6][258];
		base = new int[6][258];
		perm = new int[6][258];
		minLens = new int[6];
	}
}
