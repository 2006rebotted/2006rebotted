package com.runescape.cache.bzip;

public final class BZip2Decompressor {

	public static int decompress(byte output[], int length, byte compressed[], int decompressedLength, int minLen) {
		synchronized (state) {
			state.compressed = compressed;
			state.nextIn = minLen;
			state.decompressed = output;
			state.nextOut = 0;
			state.decompressedLength = decompressedLength;
			state.length = length;
			state.bsLive = 0;
			state.bsBuff = 0;
			state.totalInLo32 = 0;
			state.totalInHi32 = 0;
			state.totalOutLo32 = 0;
			state.totalOutHigh32 = 0;
			state.currentBlock = 0;
			decompress(state);
			length -= state.length;
			return length;
		}
	}

	private static void method226(BZip2DecompressionState state) {
		byte byte4 = state.aByte573;
		int i = state.anInt574;
		int j = state.anInt584;
		int k = state.anInt582;
		int ai[] = BZip2DecompressionState.tt;
		int l = state.anInt581;
		byte abyte0[] = state.decompressed;
		int i1 = state.nextOut;
		int j1 = state.length;
		int k1 = j1;
		int l1 = state.anInt601 + 1;
		label0: do {
			if (i > 0) {
				do {
					if (j1 == 0)
						break label0;
					if (i == 1)
						break;
					abyte0[i1] = byte4;
					i--;
					i1++;
					j1--;
				} while (true);
				if (j1 == 0) {
					i = 1;
					break;
				}
				abyte0[i1] = byte4;
				i1++;
				j1--;
			}
			boolean flag = true;
			while (flag) {
				flag = false;
				if (j == l1) {
					i = 0;
					break label0;
				}
				byte4 = (byte) k;
				l = ai[l];
				byte byte0 = (byte) (l & 0xff);
				l >>= 8;
				j++;
				if (byte0 != k) {
					k = byte0;
					if (j1 == 0) {
						i = 1;
					} else {
						abyte0[i1] = byte4;
						i1++;
						j1--;
						flag = true;
						continue;
					}
					break label0;
				}
				if (j != l1)
					continue;
				if (j1 == 0) {
					i = 1;
					break label0;
				}
				abyte0[i1] = byte4;
				i1++;
				j1--;
				flag = true;
			}
			i = 2;
			l = ai[l];
			byte byte1 = (byte) (l & 0xff);
			l >>= 8;
			if (++j != l1)
				if (byte1 != k) {
					k = byte1;
				} else {
					i = 3;
					l = ai[l];
					byte byte2 = (byte) (l & 0xff);
					l >>= 8;
					if (++j != l1)
						if (byte2 != k) {
							k = byte2;
						} else {
							l = ai[l];
							byte byte3 = (byte) (l & 0xff);
							l >>= 8;
							j++;
							i = (byte3 & 0xff) + 4;
							l = ai[l];
							k = (byte) (l & 0xff);
							l >>= 8;
							j++;
						}
				}
		} while (true);
		int i2 = state.totalOutLo32;
		state.totalOutLo32 += k1 - j1;
		if (state.totalOutLo32 < i2)
			state.totalOutHigh32++;
		state.aByte573 = byte4;
		state.anInt574 = i;
		state.anInt584 = j;
		state.anInt582 = k;
		BZip2DecompressionState.tt = ai;
		state.anInt581 = l;
		state.decompressed = abyte0;
		state.nextOut = i1;
		state.length = j1;
	}

	private static void decompress(BZip2DecompressionState state) {
		int gMinLen = 0;
		int gLimit[] = null;
		int gBase[] = null;
		int gPerm[] = null;
		state.anInt578 = 1;
		if (BZip2DecompressionState.tt == null)
			BZip2DecompressionState.tt = new int[state.anInt578 * 0x186a0];
		boolean flag19 = true;
		while (flag19) {
			byte uc = getUnsignedChar(state);
			if (uc == 23)
				return;
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			state.currentBlock++;
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			uc = getUnsignedChar(state);
			uc = getBit(state);
			state.aBoolean575 = uc != 0; 
			state.randomised = 0;
			uc = getUnsignedChar(state);
			state.randomised = state.randomised << 8 | uc & 0xff;
			uc = getUnsignedChar(state);
			state.randomised = state.randomised << 8 | uc & 0xff;
			uc = getUnsignedChar(state);
			state.randomised = state.randomised << 8 | uc & 0xff;
			for (int j = 0; j < 16; j++) {
				byte bit = getBit(state);
				state.inUse16[j] = bit == 1;
			}

			for (int k = 0; k < 256; k++)
				state.inUse[k] = false;

			for (int l = 0; l < 16; l++)
				if (state.inUse16[l]) {
					for (int i3 = 0; i3 < 16; i3++) {
						byte byte2 = getBit(state);
						if (byte2 == 1)
							state.inUse[l * 16 + i3] = true;
					}

				}

			makeMaps(state);
			int alphabetSize = state.nInUse + 2;
			/*
			 * number of different Huffman tables in use
			 */
			int huffmanTableCount = getBits(3, state);
			/*
			 * number of times that the Huffman tables are swapped (each 50 bytes)
			 */
			int swapCount = getBits(15, state);
			for (int i1 = 0; i1 < swapCount; i1++) {
				int count = 0;
				do {
					byte byte3 = getBit(state);
					if (byte3 == 0)
						break;
					count++;
				} while (true);
				state.selectorMtf[i1] = (byte) count;
			}

			byte pos[] = new byte[6];
			for (byte v = 0; v < huffmanTableCount; v++)
				pos[v] = v;

			for (int j1 = 0; j1 < swapCount; j1++) {
				byte v = state.selectorMtf[j1];
				byte tmp = pos[v];
				for (; v > 0; v--)
					pos[v] = pos[v - 1];

				pos[0] = tmp;
				state.selector[j1] = tmp;
			}

			for (int k3 = 0; k3 < huffmanTableCount; k3++) {
				int l6 = getBits(5, state);
				for (int k1 = 0; k1 < alphabetSize; k1++) {
					do {
						byte byte4 = getBit(state);
						if (byte4 == 0)
							break;
						byte4 = getBit(state);
						if (byte4 == 0)
							l6++;
						else
							l6--;
					} while (true);
					state.len[k3][k1] = (byte) l6;
				}

			}

			for (int l3 = 0; l3 < huffmanTableCount; l3++) {
				byte byte8 = 32;
				int i = 0;
				for (int l1 = 0; l1 < alphabetSize; l1++) {
					if (state.len[l3][l1] > i)
						i = state.len[l3][l1];
					if (state.len[l3][l1] < byte8)
						byte8 = state.len[l3][l1];
				}

				createDecodeTables(state.limit[l3], state.base[l3], state.perm[l3], state.len[l3], byte8, i, alphabetSize);
				state.minLens[l3] = byte8;
			}

			int l4 = state.nInUse + 1;
			int i5 = -1;
			int j5 = 0;
			for (int i2 = 0; i2 <= 255; i2++)
				state.unzftab[i2] = 0;

			int j9 = 4095;
			for (int l8 = 15; l8 >= 0; l8--) {
				for (int i9 = 15; i9 >= 0; i9--) {
					state.mtfa[j9] = (byte) (l8 * 16 + i9);
					j9--;
				}

				state.mtfbase[l8] = j9 + 1;
			}

			int i6 = 0;
			if (j5 == 0) {
				i5++;
				j5 = 50;
				byte byte12 = state.selector[i5];
				gMinLen = state.minLens[byte12];
				gLimit = state.limit[byte12];
				gPerm = state.perm[byte12];
				gBase = state.base[byte12];
			}
			j5--;
			int i7 = gMinLen;
			int l7;
			byte byte9;
			for (l7 = getBits(i7, state); l7 > gLimit[i7]; l7 = l7 << 1 | byte9) {
				i7++;
				byte9 = getBit(state);
			}

			for (int k5 = gPerm[l7 - gBase[i7]]; k5 != l4;)
				if (k5 == 0 || k5 == 1) {
					int j6 = -1;
					int k6 = 1;
					do {
						if (k5 == 0)
							j6 += k6;
						else if (k5 == 1)
							j6 += 2 * k6;
						k6 *= 2;
						if (j5 == 0) {
							i5++;
							j5 = 50;
							byte byte13 = state.selector[i5];
							gMinLen = state.minLens[byte13];
							gLimit = state.limit[byte13];
							gPerm = state.perm[byte13];
							gBase = state.base[byte13];
						}
						j5--;
						int j7 = gMinLen;
						int i8;
						byte byte10;
						for (i8 = getBits(j7, state); i8 > gLimit[j7]; i8 = i8 << 1 | byte10) {
							j7++;
							byte10 = getBit(state);
						}

						k5 = gPerm[i8 - gBase[j7]];
					} while (k5 == 0 || k5 == 1);
					j6++;
					byte byte5 = state.seqToUnseq[state.mtfa[state.mtfbase[0]] & 0xff];
					state.unzftab[byte5 & 0xff] += j6;
					for (; j6 > 0; j6--) {
						BZip2DecompressionState.tt[i6] = byte5 & 0xff;
						i6++;
					}

				} else {
					int j11 = k5 - 1;
					byte byte6;
					if (j11 < 16) {
						int j10 = state.mtfbase[0];
						byte6 = state.mtfa[j10 + j11];
						for (; j11 > 3; j11 -= 4) {
							int k11 = j10 + j11;
							state.mtfa[k11] = state.mtfa[k11 - 1];
							state.mtfa[k11 - 1] = state.mtfa[k11 - 2];
							state.mtfa[k11 - 2] = state.mtfa[k11 - 3];
							state.mtfa[k11 - 3] = state.mtfa[k11 - 4];
						}

						for (; j11 > 0; j11--)
							state.mtfa[j10 + j11] = state.mtfa[(j10 + j11) - 1];

						state.mtfa[j10] = byte6;
					} else {
						int l10 = j11 / 16;
						int i11 = j11 % 16;
						int k10 = state.mtfbase[l10] + i11;
						byte6 = state.mtfa[k10];
						for (; k10 > state.mtfbase[l10]; k10--)
							state.mtfa[k10] = state.mtfa[k10 - 1];

						state.mtfbase[l10]++;
						for (; l10 > 0; l10--) {
							state.mtfbase[l10]--;
							state.mtfa[state.mtfbase[l10]] = state.mtfa[(state.mtfbase[l10 - 1] + 16) - 1];
						}

						state.mtfbase[0]--;
						state.mtfa[state.mtfbase[0]] = byte6;
						if (state.mtfbase[0] == 0) {
							int i10 = 4095;
							for (int k9 = 15; k9 >= 0; k9--) {
								for (int l9 = 15; l9 >= 0; l9--) {
									state.mtfa[i10] = state.mtfa[state.mtfbase[k9] + l9];
									i10--;
								}

								state.mtfbase[k9] = i10 + 1;
							}

						}
					}
					state.unzftab[state.seqToUnseq[byte6 & 0xff] & 0xff]++;
					BZip2DecompressionState.tt[i6] = state.seqToUnseq[byte6 & 0xff] & 0xff;
					i6++;
					if (j5 == 0) {
						i5++;
						j5 = 50;
						byte byte14 = state.selector[i5];
						gMinLen = state.minLens[byte14];
						gLimit = state.limit[byte14];
						gPerm = state.perm[byte14];
						gBase = state.base[byte14];
					}
					j5--;
					int k7 = gMinLen;
					int j8;
					byte byte11;
					for (j8 = getBits(k7, state); j8 > gLimit[k7]; j8 = j8 << 1 | byte11) {
						k7++;
						byte11 = getBit(state);
					}

					k5 = gPerm[j8 - gBase[k7]];
				}

			state.anInt574 = 0;
			state.aByte573 = 0;
			state.cftab[0] = 0;
			for (int j2 = 1; j2 <= 256; j2++)
				state.cftab[j2] = state.unzftab[j2 - 1];

			for (int k2 = 1; k2 <= 256; k2++)
				state.cftab[k2] += state.cftab[k2 - 1];

			for (int l2 = 0; l2 < i6; l2++) {
				byte byte7 = (byte) (BZip2DecompressionState.tt[l2] & 0xff);
				BZip2DecompressionState.tt[state.cftab[byte7 & 0xff]] |= l2 << 8;
				state.cftab[byte7 & 0xff]++;
			}

			state.anInt581 = BZip2DecompressionState.tt[state.randomised] >> 8;
			state.anInt584 = 0;
			state.anInt581 = BZip2DecompressionState.tt[state.anInt581];
			state.anInt582 = (byte) (state.anInt581 & 0xff);
			state.anInt581 >>= 8;
			state.anInt584++;
			state.anInt601 = i6;
			method226(state);
			flag19 = state.anInt584 == state.anInt601 + 1 && state.anInt574 == 0;
		}
	}

	private static byte getUnsignedChar(BZip2DecompressionState state) {
		return (byte) getBits(8, state);
	}

	private static byte getBit(BZip2DecompressionState state) {
		return (byte) getBits(1, state);
	}

	private static int getBits(int i, BZip2DecompressionState state) {
		int j;
		do {
			if (state.bsLive >= i) {
				int k = state.bsBuff >> state.bsLive - i & (1 << i) - 1;
				state.bsLive -= i;
				j = k;
				break;
			}
			state.bsBuff = state.bsBuff << 8 | state.compressed[state.nextIn] & 0xff;
			state.bsLive += 8;
			state.nextIn++;
			state.decompressedLength--;
			state.totalInLo32++;
			if (state.totalInLo32 == 0)
				state.totalInHi32++;
		} while (true);
		return j;
	}

	private static void makeMaps(BZip2DecompressionState state) {
		state.nInUse = 0;
		for (int i = 0; i < 256; i++)
			if (state.inUse[i]) {
				state.seqToUnseq[state.nInUse] = (byte) i;
				state.nInUse++;
			}

	}

	private static void createDecodeTables(int limit[], int base[], int perm[], byte length[], int i, int maxLength, int alphabetSize) {
		int pp = 0;
		for (int i1 = i; i1 <= maxLength; i1++) {
			for (int l2 = 0; l2 < alphabetSize; l2++) {
				if (length[l2] == i1) {
					perm[pp] = l2;
					pp++;
				}
			}
		}

		for (int j1 = 0; j1 < 23; j1++)
			base[j1] = 0;

		for (int k1 = 0; k1 < alphabetSize; k1++)
			base[length[k1] + 1]++;

		for (int l1 = 1; l1 < 23; l1++)
			base[l1] += base[l1 - 1];

		for (int i2 = 0; i2 < 23; i2++)
			limit[i2] = 0;

		int vec = 0;
		for (int j2 = i; j2 <= maxLength; j2++) {
			vec += base[j2 + 1] - base[j2];
			limit[j2] = vec - 1;
			vec <<= 1;
		}

		for (int k2 = i + 1; k2 <= maxLength; k2++)
			base[k2] = (limit[k2 - 1] + 1 << 1) - base[k2];

	}

	private static final BZip2DecompressionState state = new BZip2DecompressionState();

}
