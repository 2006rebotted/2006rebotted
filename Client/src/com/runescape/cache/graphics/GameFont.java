package com.runescape.cache.graphics;

import java.util.Random;

import com.runescape.cache.FileArchive;
import com.runescape.draw.Rasterizer2D;
import com.runescape.io.Buffer;

public final class GameFont extends Rasterizer2D {

	public GameFont(boolean flag, String s, FileArchive streamLoader) {
	aByteArrayArray1491 = new byte[256][];
	anIntArray1492 = new int[256];
	anIntArray1493 = new int[256];
	anIntArray1494 = new int[256];
	anIntArray1495 = new int[256];
	anIntArray1496 = new int[256];
	aRandom1498 = new Random();
	aBoolean1499 = false;
	Buffer stream = new Buffer(streamLoader.readFile(s + ".dat"));
	Buffer stream_1 = new Buffer(streamLoader.readFile("index.dat"));
	stream_1.currentPosition = stream.readUShort() + 4;
	int k = stream_1.readUnsignedByte();
	if(k > 0)
		stream_1.currentPosition += 3 * (k - 1);
	for(int l = 0; l < 256; l++) {
		anIntArray1494[l] = stream_1.readUnsignedByte();
		anIntArray1495[l] = stream_1.readUnsignedByte();
		int i1 = anIntArray1492[l] = stream_1.readUShort();
		int j1 = anIntArray1493[l] = stream_1.readUShort();
		int k1 = stream_1.readUnsignedByte();
		int l1 = i1 * j1;
		aByteArrayArray1491[l] = new byte[l1];
		if(k1 == 0) {
			for(int i2 = 0; i2 < l1; i2++)
				aByteArrayArray1491[l][i2] = stream.readSignedByte();

			} else if(k1 == 1) {
				for(int j2 = 0; j2 < i1; j2++) {
					for(int l2 = 0; l2 < j1; l2++)
					aByteArrayArray1491[l][j2 + l2 * i1] = stream.readSignedByte();
				}
			}
			if(j1 > verticalSpace && l < 128)
				verticalSpace = j1;
			anIntArray1494[l] = 1;
			anIntArray1496[l] = i1 + 2;
			int k2 = 0;
			for(int i3 = j1 / 7; i3 < j1; i3++)
				k2 += aByteArrayArray1491[l][i3 * i1];
			if(k2 <= j1 / 7) {
				anIntArray1496[l]--;
				anIntArray1494[l] = 0;
			}
			k2 = 0;
			for(int j3 = j1 / 7; j3 < j1; j3++)
				k2 += aByteArrayArray1491[l][(i1 - 1) + j3 * i1];
			if(k2 <= j1 / 7)
				anIntArray1496[l]--;
		}
		if(flag) {
			anIntArray1496[32] = anIntArray1496[73];
		} else {
			anIntArray1496[32] = anIntArray1496[105];
		}
	}

	public void method380(String s, int i, int j, int k) {
		render(j, s, k, i - method384(s));
	}

	public void drawText(int i, String s, int k, int l) {
		render(i, s, k, l - method384(s) / 2);
	}

	public void method382(int i, int j, String s, int l, boolean flag) {
		drawTextWithPotentialShadow(flag, j - getTextWidth(s) / 2, i, s, l);
	}

	public void drawChatInput(int i, int j, String s, int l, boolean flag) {
		drawTextWithPotentialShadow(flag, j, i, s, l);
	}

	public int getTextWidth(String s) {
		if(s == null)
			return 0;
		int j = 0;
		for(int k = 0; k < s.length(); k++)
			if(s.charAt(k) == '@' && k + 4 < s.length() && s.charAt(k + 4) == '@')
				k += 4;
			else
				j += anIntArray1496[s.charAt(k)];
		return j;
	}

	public int method384(String s) {
		if(s == null)
			return 0;
		int j = 0;
		for(int k = 0; k < s.length(); k++)
			j += anIntArray1496[s.charAt(k)];
		return j;
	}

	public void render(int i, String s, int j, int l) {
		if(s == null)
			return;
		j -= verticalSpace;
		for(int i1 = 0; i1 < s.length(); i1++) {
			char c = s.charAt(i1);
			if(c != ' ')
				method392(aByteArrayArray1491[c], l + anIntArray1494[c], j + anIntArray1495[c], anIntArray1492[c], anIntArray1493[c], i);
			l += anIntArray1496[c];
		}
	}

	public void wave(int i, String s, int j, int k, int l) {
		if(s == null)
			return;
		j -= method384(s) / 2;
		l -= verticalSpace;
		for(int i1 = 0; i1 < s.length(); i1++) {
			char c = s.charAt(i1);
			if(c != ' ')
				method392(aByteArrayArray1491[c], j + anIntArray1494[c], l + anIntArray1495[c] + (int)(Math.sin((double)i1 / 2D + (double)k / 5D) * 5D), anIntArray1492[c], anIntArray1493[c], i);
			j += anIntArray1496[c];
		}
	}

	public void wave2(int i, String s, int j, int k, int l) {
		if(s == null)
			return;
		i -= method384(s) / 2;
		k -= verticalSpace;
		for(int i1 = 0; i1 < s.length(); i1++) {
			char c = s.charAt(i1);
			if(c != ' ')
				method392(aByteArrayArray1491[c], i + anIntArray1494[c] + (int)(Math.sin((double)i1 / 5D + (double)j / 5D) * 5D), k + anIntArray1495[c] + (int)(Math.sin((double)i1 / 3D + (double)j / 5D) * 5D), anIntArray1492[c], anIntArray1493[c], l);
			i += anIntArray1496[c];
		}
	}

	public void shake(int i, String s, int j, int k, int l, int i1) {
		if(s == null)
			return;
		double d = 7D - (double)i / 8D;
		if(d < 0.0D)
			d = 0.0D;
		l -= method384(s) / 2;
		k -= verticalSpace;
		for(int k1 = 0; k1 < s.length(); k1++) {
			char c = s.charAt(k1);
			if(c != ' ')
				method392(aByteArrayArray1491[c], l + anIntArray1494[c], k + anIntArray1495[c] + (int)(Math.sin((double)k1 / 1.5D + (double)j) * d), anIntArray1492[c], anIntArray1493[c], i1);
			l += anIntArray1496[c];
		}
	}

	public void drawTextWithPotentialShadow(boolean flag1, int i, int j, String s, int k) {
		aBoolean1499 = false;
		int l = i;
		if(s == null)
			return;
		k -= verticalSpace;
		for(int i1 = 0; i1 < s.length(); i1++)
			if(s.charAt(i1) == '@' && i1 + 4 < s.length() && s.charAt(i1 + 4) == '@') {
				int j1 = getColorByName(s.substring(i1 + 1, i1 + 4));
				if(j1 != -1)
					j = j1;
				i1 += 4;
			} else {
				char c = s.charAt(i1);
				if(c != ' ') {
					if(flag1)
					method392(aByteArrayArray1491[c], i + anIntArray1494[c] + 1, k + anIntArray1495[c] + 1, anIntArray1492[c], anIntArray1493[c], 0);
					method392(aByteArrayArray1491[c], i + anIntArray1494[c], k + anIntArray1495[c], anIntArray1492[c], anIntArray1493[c], j);
				}
				i += anIntArray1496[c];
			}
		if(aBoolean1499)
			Rasterizer2D.drawHorizontalLine(l, k + (int)((double)verticalSpace * 0.69999999999999996D), i - l, 0x800000);
	}

	public void method390(int i, int j, String s, int k, int i1) {
		if(s == null)
			return;
		aRandom1498.setSeed(k);
		int j1 = 192 + (aRandom1498.nextInt() & 0x1f);
		i1 -= verticalSpace;
		for(int k1 = 0; k1 < s.length(); k1++)
			if(s.charAt(k1) == '@' && k1 + 4 < s.length() && s.charAt(k1 + 4) == '@') {
				int l1 = getColorByName(s.substring(k1 + 1, k1 + 4));
				if(l1 != -1)
					j = l1;
				k1 += 4;
			} else {
				char c = s.charAt(k1);
				if(c != ' ') {
					method394(192, i + anIntArray1494[c] + 1, aByteArrayArray1491[c], anIntArray1492[c], i1 + anIntArray1495[c] + 1, anIntArray1493[c], 0);
					method394(j1, i + anIntArray1494[c], aByteArrayArray1491[c], anIntArray1492[c], i1 + anIntArray1495[c], anIntArray1493[c], j);
				}
				i += anIntArray1496[c];
				if((aRandom1498.nextInt() & 3) == 0)
					i++;
			}
	}

	private int getColorByName(String s) {
		if(s.equals("369"))//color code, use as @###@
			return 0x336699;//hex code
		if(s.equals("mon"))
			return 0x00ff80;
		if(s.equals("red"))
			return 0xff0000;
		if(s.equals("gre"))
			return 65280;
		if(s.equals("blu"))
			return 255;
		if(s.equals("yel"))
			return 0xffff00;
		if(s.equals("cya"))
			return 65535;
		if(s.equals("mag"))
			return 0xff00ff;
		if(s.equals("whi"))
			return 0xffffff;
		if(s.equals("bla"))
			return 0;
		if(s.equals("lre"))
			return 0xff9040;
		if(s.equals("dre"))
			return 0x800000;
		if(s.equals("dbl"))
			return 128;
		if(s.equals("or1"))
			return 0xffb000;
		if(s.equals("or2"))
			return 0xff7000;
		if(s.equals("or3"))
			return 0xff3000;
		if(s.equals("gr1"))
			return 0xc0ff00;
		if(s.equals("gr2"))
			return 0x80ff00;
		if(s.equals("gr3"))
			return 0x40ff00;
		if(s.equals("str"))
			aBoolean1499 = true;
		if(s.equals("end"))
			aBoolean1499 = false;
		return -1;
	}

	private void method392(byte abyte0[], int i, int j, int k, int l, int i1) {
        int j1 = i + j * width;
        int k1 = width - k;
        int l1 = 0;
        int i2 = 0;
        if (j < topY) {
            int j2 = topY - j;
            l -= j2;
            j = topY;
            i2 += j2 * k;
            j1 += j2 * width;
        }
        if (j + l >= bottomY)
            l -= ((j + l) - bottomY);
        if (i < leftX) {
            int k2 = leftX - i;
            k -= k2;
            i = leftX;
            i2 += k2;
            j1 += k2;
            l1 += k2;
            k1 += k2;
        }
        if (i + k >= bottomX) {
            int l2 = ((i + k) - bottomX);
            k -= l2;
            l1 += l2;
            k1 += l2;
        }
        if (!(k <= 0 || l <= 0)) {
            method393(pixels, abyte0, i1, i2, j1, k, l, k1, l1);
        }
    }

	private void method393(int ai[], byte abyte0[], int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1 = -(l >> 2);
		l = -(l & 3);
		for(int i2 = -i1; i2 < 0; i2++) {
			for(int j2 = l1; j2 < 0; j2++) {
				if(abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
				if(abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
				if(abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
				if(abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
			}
			for(int k2 = l; k2 < 0; k2++)
			if(abyte0[j++] != 0)
				ai[k++] = i;
			else
				k++;

			k += j1;
			j += k1;
		}
	}

	private void method394(int i, int j, byte abyte0[], int k, int l, int i1,
                           int j1) {
        int k1 = j + l * width;
        int l1 = width - k;
        int i2 = 0;
        int j2 = 0;
        if (l < topY) {
            int k2 = topY - l;
            i1 -= k2;
            l = topY;
            j2 += k2 * k;
            k1 += k2 * width;
        }
        if (l + i1 >= bottomY)
            i1 -= ((l + i1) - bottomY);
        if (j < leftX) {
            int l2 = leftX - j;
            k -= l2;
            j = leftX;
            j2 += l2;
            k1 += l2;
            i2 += l2;
            l1 += l2;
        }
        if (j + k >= bottomX) {
            int i3 = ((j + k) - bottomX);
            k -= i3;
            i2 += i3;
            l1 += i3;
        }
        if (k <= 0 || i1 <= 0)
            return;
        method395(abyte0, i1, k1, pixels, j2, k, i2, l1, j1, i);
    }

	private void method395(byte abyte0[], int i, int j, int ai[], int l, int i1, int j1, int k1, int l1, int i2) {
		l1 = ((l1 & 0xff00ff) * i2 & 0xff00ff00) + ((l1 & 0xff00) * i2 & 0xff0000) >> 8;
		i2 = 256 - i2;
		for(int j2 = -i; j2 < 0; j2++) {
				for(int k2 = -i1; k2 < 0; k2++)
					if(abyte0[l++] != 0) {
						int l2 = ai[j];
						ai[j++] = (((l2 & 0xff00ff) * i2 & 0xff00ff00) + ((l2 & 0xff00) * i2 & 0xff0000) >> 8) + l1;
					} else {
						j++;
					}
				j += k1;
				l += j1;
		}
	}

	public byte[][] aByteArrayArray1491;
	public int[] anIntArray1492;
	public int[] anIntArray1493;
	public int[] anIntArray1494;
	public int[] anIntArray1495;
	public int[] anIntArray1496;
	public int verticalSpace;	
	public Random aRandom1498;
	public boolean aBoolean1499;
}
