package com.runescape.draw;

import com.runescape.cache.FileArchive;
import com.runescape.cache.graphics.IndexedImage;
import com.runescape.scene.SceneGraph;

public final class Rasterizer3D extends Rasterizer2D {

	public static void clear() {
		anIntArray1468 = null;
		anIntArray1468 = null;
		anIntArray1470 = null;
		COSINE = null;
		scanOffsets = null;
		textures = null;
		textureIsTransparant = null;
		averageTextureColours = null;
		textureRequestPixelBuffer = null;
		texturesPixelBuffer = null;
		textureLastUsed = null;
		hslToRgb = null;
		currentPalette = null;
	}

	public static void useViewport() {
		scanOffsets = new int[Rasterizer2D.height];
		
		for (int j = 0; j < Rasterizer2D.height; j++) {
			scanOffsets[j] = Rasterizer2D.width * j;			
		}

		originViewX = Rasterizer2D.width / 2;
		originViewY = Rasterizer2D.height / 2;
	}

	public static void reposition(int width, int length) {
		scanOffsets = new int[length];
		for (int x = 0; x < length; x++) {
			scanOffsets[x] = width * x;
		}
		originViewX = width / 2;
		originViewY = length / 2;
	}

	public static void clearTextureCache() {
		textureRequestPixelBuffer = null;
		for (int i = 0; i < 50; i++)
            texturesPixelBuffer[i] = null;
	}

	public static void initiateRequestBuffers() {
		if (textureRequestPixelBuffer == null) {
            textureRequestBufferPointer = 20;
			if (lowMem)
				textureRequestPixelBuffer = new int[textureRequestBufferPointer][16384];
			else
				textureRequestPixelBuffer = new int[textureRequestBufferPointer][0x10000];
			for (int i = 0; i < 50; i++)
				texturesPixelBuffer[i] = null;
		}
	}

	public static void loadTextures(FileArchive archive) {		
		textureCount = 0;
		for (int index = 0; index < 51; index++) {			
			try {				
				textures[index] = new IndexedImage(archive, String.valueOf(index), 0);
				if (lowMem && textures[index].resizeWidth == 128) {
					textures[index].downscale();
				} else {
					textures[index].resize();
				}
				textureCount++;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static int getOverallColour(int textureId) {
		if (averageTextureColours[textureId] != 0)
			return averageTextureColours[textureId];
		int totalRed = 0;
		int totalGreen = 0;
		int totalBlue = 0;
		int colourCount = currentPalette[textureId].length;
		for (int ptr = 0; ptr < colourCount; ptr++) {
			totalRed += currentPalette[textureId][ptr] >> 16 & 0xff;
			totalGreen += currentPalette[textureId][ptr] >> 8 & 0xff;
			totalBlue += currentPalette[textureId][ptr] & 0xff;
		}

		int avgPaletteColour = (totalRed / colourCount << 16) + (totalGreen / colourCount << 8) + totalBlue / colourCount;
		avgPaletteColour = adjustBrightness(avgPaletteColour, 1.3999999999999999D);
		if (avgPaletteColour == 0)
			avgPaletteColour = 1;
		averageTextureColours[textureId] = avgPaletteColour;
		return avgPaletteColour;
	}

	public static void requestTextureUpdate(int textureId) {
        if (texturesPixelBuffer[textureId] == null) {
            return;
        }
        textureRequestPixelBuffer[textureRequestBufferPointer++] = texturesPixelBuffer[textureId];
        texturesPixelBuffer[textureId] = null;
	}

	private static int[] getTexturePixels(int textureId) {
        textureLastUsed[textureId] = lastTextureRetrievalCount++;
		if (texturesPixelBuffer[textureId] != null)
			return texturesPixelBuffer[textureId];
		int texturePixels[];
		if (textureRequestBufferPointer > 0) {
			texturePixels = textureRequestPixelBuffer[--textureRequestBufferPointer];
			textureRequestPixelBuffer[textureRequestBufferPointer] = null;
		} else {
			int lastUsed = 0;
			int target = -1;
			for (int l = 0; l < textureCount; l++)
				if (texturesPixelBuffer[l] != null && (textureLastUsed[l] < lastUsed || target == -1)) {
					lastUsed = textureLastUsed[l];
					target = l;
				}

			texturePixels = texturesPixelBuffer[target];
			texturesPixelBuffer[target] = null;
		}
		texturesPixelBuffer[textureId] = texturePixels;
		IndexedImage background = textures[textureId];
		int texturePalette[] = currentPalette[textureId];
		if (lowMem) {
            textureIsTransparant[textureId] = false;
			for (int i1 = 0; i1 < 4096; i1++) {
				int colour = texturePixels[i1] = texturePalette[background.palettePixels[i1]] & 0xf8f8ff;
				if (colour == 0)
					textureIsTransparant[textureId] = true;
				texturePixels[4096 + i1] = colour - (colour >>> 3) & 0xf8f8ff;
				texturePixels[8192 + i1] = colour - (colour >>> 2) & 0xf8f8ff;
				texturePixels[12288 + i1] = colour - (colour >>> 2) - (colour >>> 3) & 0xf8f8ff;
			}

		} else {
			if (background.width == 64) {
				for (int x = 0; x < 128; x++) {
					for (int y = 0; y < 128; y++)
						texturePixels[y + (x << 7)] = texturePalette[background.palettePixels[(y >> 1) + ((x >> 1) << 6)]];
				}
			} else {
				for (int i = 0; i < 16384; i++)
					texturePixels[i] = texturePalette[background.palettePixels[i]];
			}
			textureIsTransparant[textureId] = false;
			for (int i = 0; i < 16384; i++) {
				texturePixels[i] &= 0xf8f8ff;
				int colour = texturePixels[i];
				if (colour == 0)
					textureIsTransparant[textureId] = true;
				texturePixels[16384 + i] = colour - (colour >>> 3) & 0xf8f8ff;
				texturePixels[32768 + i] = colour - (colour >>> 2) & 0xf8f8ff;
				texturePixels[49152 + i] = colour - (colour >>> 2) - (colour >>> 3) & 0xf8f8ff;
			}

		}
		return texturePixels;
	}

	public static void setBrightness(double brightness) {
		int j = 0;
		for (int k = 0; k < 512; k++) {
			double d1 = (double) (k / 8) / 64D + 0.0078125D;
			double d2 = (double) (k & 7) / 8D + 0.0625D;
			for (int k1 = 0; k1 < 128; k1++) {
				double d3 = (double) k1 / 128D;
				double r = d3;
				double g = d3;
				double b = d3;
				if (d2 != 0.0D) {
					double d7;
					if (d3 < 0.5D)
						d7 = d3 * (1.0D + d2);
					else
						d7 = (d3 + d2) - d3 * d2;
					double d8 = 2D * d3 - d7;
					double d9 = d1 + 0.33333333333333331D;
					if (d9 > 1.0D)
						d9--;
					double d10 = d1;
					double d11 = d1 - 0.33333333333333331D;
					if (d11 < 0.0D)
						d11++;
					if (6D * d9 < 1.0D)
						r = d8 + (d7 - d8) * 6D * d9;
					else if (2D * d9 < 1.0D)
						r = d7;
					else if (3D * d9 < 2D)
						r = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
					else
						r = d8;
					if (6D * d10 < 1.0D)
						g = d8 + (d7 - d8) * 6D * d10;
					else if (2D * d10 < 1.0D)
						g = d7;
					else if (3D * d10 < 2D)
						g = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
					else
						g = d8;
					if (6D * d11 < 1.0D)
						b = d8 + (d7 - d8) * 6D * d11;
					else if (2D * d11 < 1.0D)
						b = d7;
					else if (3D * d11 < 2D)
						b = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
					else
						b = d8;
				}
				int byteR = (int) (r * 256D);
				int byteG = (int) (g * 256D);
				int byteB = (int) (b * 256D);
				int rgb = (byteR << 16) + (byteG << 8) + byteB;
				rgb = adjustBrightness(rgb, brightness);
				if (rgb == 0)
					rgb = 1;
                hslToRgb[j++] = rgb;
			}

		}

		for (int textureId = 0; textureId < 51; textureId++)
			if (textures[textureId] != null) {
				int originalPalette[] = textures[textureId].palette;
				currentPalette[textureId] = new int[originalPalette.length];
				for (int colourId = 0; colourId < originalPalette.length; colourId++) {
					currentPalette[textureId][colourId] = adjustBrightness(originalPalette[colourId], brightness);
					if ((currentPalette[textureId][colourId] & 0xf8f8ff) == 0 && colourId != 0)
						currentPalette[textureId][colourId] = 1;
				}

			}

		for (int textureId = 0; textureId < 51; textureId++)
			requestTextureUpdate(textureId);

	}

	private static int adjustBrightness(int rgb, double intensity) {
		double r = (double) (rgb >> 16) / 256D;
		double g = (double) (rgb >> 8 & 0xff) / 256D;
		double b = (double) (rgb & 0xff) / 256D;
		r = Math.pow(r, intensity);
		g = Math.pow(g, intensity);
		b = Math.pow(b, intensity);
		int r_byte = (int) (r * 256D);
		int g_byte = (int) (g * 256D);
		int b_byte = (int) (b * 256D);
		return (r_byte << 16) + (g_byte << 8) + b_byte;
	}

	public static void drawShadedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int hsl1, int hsl2, int hsl3, float z_a, float z_b, float z_c) {
		if (z_a < 0 || z_b < 0 || z_c < 0)
			return;
		int rgb1 = hslToRgb[hsl1];
		int rgb2 = hslToRgb[hsl2];
		int rgb3 = hslToRgb[hsl3];
		int r1 = rgb1 >> 16 & 0xff;
		int g1 = rgb1 >> 8 & 0xff;
		int b1 = rgb1 & 0xff;
		int r2 = rgb2 >> 16 & 0xff;
		int g2 = rgb2 >> 8 & 0xff;
		int b2 = rgb2 & 0xff;
		int r3 = rgb3 >> 16 & 0xff;
		int g3 = rgb3 >> 8 & 0xff;
		int b3 = rgb3 & 0xff;
		int a_to_b = 0;
		int dr1 = 0;
		int dg1 = 0;
		int db1 = 0;
		if (y_b != y_a) {
			a_to_b = (x_b - x_a << 16) / (y_b - y_a);
			dr1 = (r2 - r1 << 16) / (y_b - y_a);
			dg1 = (g2 - g1 << 16) / (y_b - y_a);
			db1 = (b2 - b1 << 16) / (y_b - y_a);
		}
		int b_to_c = 0;
		int dr2 = 0;
		int dg2 = 0;
		int db2 = 0;
		if (y_c != y_b) {
			b_to_c = (x_c - x_b << 16) / (y_c - y_b);
			dr2 = (r3 - r2 << 16) / (y_c - y_b);
			dg2 = (g3 - g2 << 16) / (y_c - y_b);
			db2 = (b3 - b2 << 16) / (y_c - y_b);
		}
		int c_to_a = 0;
		int dr3 = 0;
		int dg3 = 0;
		int db3 = 0;
		if (y_c != y_a) {
			c_to_a = (x_a - x_c << 16) / (y_a - y_c);
			dr3 = (r1 - r3 << 16) / (y_a - y_c);
			dg3 = (g1 - g3 << 16) / (y_a - y_c);
			db3 = (b1 - b3 << 16) / (y_a - y_c);
		}
		float b_aX = x_b - x_a;
		float b_aY = y_b - y_a;
		float c_aX = x_c - x_a;
		float c_aY = y_c - y_a;
		float b_aZ = z_b - z_a;
		float c_aZ = z_c - z_a;

		float div = b_aX * c_aY - c_aX * b_aY;
		float depth_slope = (b_aZ * c_aY - c_aZ * b_aY) / div;
		float depth_increment = (c_aZ * b_aX - b_aZ * c_aX) / div;
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= Rasterizer2D.bottomY) {
				return;
			}
			if (y_b > Rasterizer2D.bottomY) {
				y_b = Rasterizer2D.bottomY;
			}
			if (y_c > Rasterizer2D.bottomY) {
				y_c = Rasterizer2D.bottomY;
			}
			z_a = z_a - depth_slope * x_a + depth_slope;
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				r3 = r1 <<= 16;
				g3 = g1 <<= 16;
				b3 = b1 <<= 16;
				if (y_a < 0) {
					x_c -= c_to_a * y_a;
					x_a -= a_to_b * y_a;
					r3 -= dr3 * y_a;
					g3 -= dg3 * y_a;
					b3 -= db3 * y_a;
					r1 -= dr1 * y_a;
					g1 -= dg1 * y_a;
					b1 -= db1 * y_a;
					z_a -= depth_increment * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				r2 <<= 16;
				g2 <<= 16;
				b2 <<= 16;
				if (y_b < 0) {
					x_b -= b_to_c * y_b;
					r2 -= dr2 * y_b;
					g2 -= dg2 * y_b;
					b2 -= db2 * y_b;
					y_b = 0;
				}
				if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
					y_c -= y_b;
					y_b -= y_a;
					for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
						drawShadedScanline(Rasterizer2D.pixels, y_a, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1, z_a,
								depth_slope);
						x_c += c_to_a;
						x_a += a_to_b;
						r3 += dr3;
						g3 += dg3;
						b3 += db3;
						r1 += dr1;
						g1 += dg1;
						b1 += db1;
						z_a += depth_increment;
					}
					while (--y_c >= 0) {
						drawShadedScanline(Rasterizer2D.pixels, y_a, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2, z_a,
								depth_slope);
						x_c += c_to_a;
						x_b += b_to_c;
						r3 += dr3;
						g3 += dg3;
						b3 += db3;
						r2 += dr2;
						g2 += dg2;
						b2 += db2;
						y_a += Rasterizer2D.width;
						z_a += depth_increment;
					}
					return;
				}
				y_c -= y_b;
				y_b -= y_a;
				for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
					drawShadedScanline(Rasterizer2D.pixels, y_a, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3, z_a,
							depth_slope);
					x_c += c_to_a;
					x_a += a_to_b;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					z_a += depth_increment;
				}
				while (--y_c >= 0) {
					drawShadedScanline(Rasterizer2D.pixels, y_a, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3, z_a,
							depth_slope);
					x_c += c_to_a;
					x_b += b_to_c;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					y_a += Rasterizer2D.width;
					z_a += depth_increment;
				}
				return;
			}
			x_b = x_a <<= 16;
			r2 = r1 <<= 16;
			g2 = g1 <<= 16;
			b2 = b1 <<= 16;
			if (y_a < 0) {
				x_b -= c_to_a * y_a;
				x_a -= a_to_b * y_a;
				r2 -= dr3 * y_a;
				g2 -= dg3 * y_a;
				b2 -= db3 * y_a;
				r1 -= dr1 * y_a;
				g1 -= dg1 * y_a;
				b1 -= db1 * y_a;
				z_a -= depth_increment * y_a;
				y_a = 0;
			}
			x_c <<= 16;
			r3 <<= 16;
			g3 <<= 16;
			b3 <<= 16;
			if (y_c < 0) {
				x_c -= b_to_c * y_c;
				r3 -= dr2 * y_c;
				g3 -= dg2 * y_c;
				b3 -= db2 * y_c;
				y_c = 0;
			}
			if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
				y_b -= y_c;
				y_c -= y_a;
				for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
					drawShadedScanline(Rasterizer2D.pixels, y_a, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1, z_a,
							depth_slope);
					x_b += c_to_a;
					x_a += a_to_b;
					r2 += dr3;
					g2 += dg3;
					b2 += db3;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					z_a += depth_increment;
				}
				while (--y_b >= 0) {
					drawShadedScanline(Rasterizer2D.pixels, y_a, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1, z_a,
							depth_slope);
					x_c += b_to_c;
					x_a += a_to_b;
					r3 += dr2;
					g3 += dg2;
					b3 += db2;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					y_a += Rasterizer2D.width;
					z_a += depth_increment;
				}
				return;
			}
			y_b -= y_c;
			y_c -= y_a;
			for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
				drawShadedScanline(Rasterizer2D.pixels, y_a, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2, z_a, depth_slope);
				x_b += c_to_a;
				x_a += a_to_b;
				r2 += dr3;
				g2 += dg3;
				b2 += db3;
				r1 += dr1;
				g1 += dg1;
				b1 += db1;
				z_a += depth_increment;
			}
			while (--y_b >= 0) {
				drawShadedScanline(Rasterizer2D.pixels, y_a, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3, z_a, depth_slope);
				x_c += b_to_c;
				x_a += a_to_b;
				r3 += dr2;
				g3 += dg2;
				b3 += db2;
				r1 += dr1;
				g1 += dg1;
				b1 += db1;
				y_a += Rasterizer2D.width;
				z_a += depth_increment;
			}
			return;
		}
		if (y_b <= y_c) {
			if (y_b >= Rasterizer2D.bottomY) {
				return;
			}
			if (y_c > Rasterizer2D.bottomY) {
				y_c = Rasterizer2D.bottomY;
			}
			if (y_a > Rasterizer2D.bottomY) {
				y_a = Rasterizer2D.bottomY;
			}
			z_b = z_b - depth_slope * x_b + depth_slope;
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				r1 = r2 <<= 16;
				g1 = g2 <<= 16;
				b1 = b2 <<= 16;
				if (y_b < 0) {
					x_a -= a_to_b * y_b;
					x_b -= b_to_c * y_b;
					r1 -= dr1 * y_b;
					g1 -= dg1 * y_b;
					b1 -= db1 * y_b;
					r2 -= dr2 * y_b;
					g2 -= dg2 * y_b;
					b2 -= db2 * y_b;
					z_b -= depth_increment * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				r3 <<= 16;
				g3 <<= 16;
				b3 <<= 16;
				if (y_c < 0) {
					x_c -= c_to_a * y_c;
					r3 -= dr3 * y_c;
					g3 -= dg3 * y_c;
					b3 -= db3 * y_c;
					y_c = 0;
				}
				if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
					y_a -= y_c;
					y_c -= y_b;
					for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
						drawShadedScanline(Rasterizer2D.pixels, y_b, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2, z_b,
								depth_slope);
						x_a += a_to_b;
						x_b += b_to_c;
						r1 += dr1;
						g1 += dg1;
						b1 += db1;
						r2 += dr2;
						g2 += dg2;
						b2 += db2;
						z_b += depth_increment;
					}
					while (--y_a >= 0) {
						drawShadedScanline(Rasterizer2D.pixels, y_b, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3, z_b,
								depth_slope);
						x_a += a_to_b;
						x_c += c_to_a;
						r1 += dr1;
						g1 += dg1;
						b1 += db1;
						r3 += dr3;
						g3 += dg3;
						b3 += db3;
						y_b += Rasterizer2D.width;
						z_b += depth_increment;
					}
					return;
				}
				y_a -= y_c;
				y_c -= y_b;
				for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
					drawShadedScanline(Rasterizer2D.pixels, y_b, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1, z_b,
							depth_slope);
					x_a += a_to_b;
					x_b += b_to_c;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					z_b += depth_increment;
				}
				while (--y_a >= 0) {
					drawShadedScanline(Rasterizer2D.pixels, y_b, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1, z_b,
							depth_slope);
					x_a += a_to_b;
					x_c += c_to_a;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					y_b += Rasterizer2D.width;
					z_b += depth_increment;
				}
				return;
			}
			x_c = x_b <<= 16;
			r3 = r2 <<= 16;
			g3 = g2 <<= 16;
			b3 = b2 <<= 16;
			if (y_b < 0) {
				x_c -= a_to_b * y_b;
				x_b -= b_to_c * y_b;
				r3 -= dr1 * y_b;
				g3 -= dg1 * y_b;
				b3 -= db1 * y_b;
				r2 -= dr2 * y_b;
				g2 -= dg2 * y_b;
				b2 -= db2 * y_b;
				z_b -= depth_increment * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			r1 <<= 16;
			g1 <<= 16;
			b1 <<= 16;
			if (y_a < 0) {
				x_a -= c_to_a * y_a;
				r1 -= dr3 * y_a;
				g1 -= dg3 * y_a;
				b1 -= db3 * y_a;
				y_a = 0;
			}
			if (a_to_b < b_to_c) {
				y_c -= y_a;
				y_a -= y_b;
				for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
					drawShadedScanline(Rasterizer2D.pixels, y_b, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2, z_b,
							depth_slope);
					x_c += a_to_b;
					x_b += b_to_c;
					r3 += dr1;
					g3 += dg1;
					b3 += db1;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					z_b += depth_increment;
				}
				while (--y_c >= 0) {
					drawShadedScanline(Rasterizer2D.pixels, y_b, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2, z_b,
							depth_slope);
					x_a += c_to_a;
					x_b += b_to_c;
					r1 += dr3;
					g1 += dg3;
					b1 += db3;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					y_b += Rasterizer2D.width;
					z_b += depth_increment;
				}
				return;
			}
			y_c -= y_a;
			y_a -= y_b;
			for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
				drawShadedScanline(Rasterizer2D.pixels, y_b, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3, z_b, depth_slope);
				x_c += a_to_b;
				x_b += b_to_c;
				r3 += dr1;
				g3 += dg1;
				b3 += db1;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				z_b += depth_increment;
			}
			while (--y_c >= 0) {
				drawShadedScanline(Rasterizer2D.pixels, y_b, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1, z_b, depth_slope);
				x_a += c_to_a;
				x_b += b_to_c;
				r1 += dr3;
				g1 += dg3;
				b1 += db3;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				y_b += Rasterizer2D.width;
				z_b += depth_increment;
			}
			return;
		}
		if (y_c >= Rasterizer2D.bottomY) {
			return;
		}
		if (y_a > Rasterizer2D.bottomY) {
			y_a = Rasterizer2D.bottomY;
		}
		if (y_b > Rasterizer2D.bottomY) {
			y_b = Rasterizer2D.bottomY;
		}
		z_c = z_c - depth_slope * x_c + depth_slope;
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			r2 = r3 <<= 16;
			g2 = g3 <<= 16;
			b2 = b3 <<= 16;
			if (y_c < 0) {
				x_b -= b_to_c * y_c;
				x_c -= c_to_a * y_c;
				r2 -= dr2 * y_c;
				g2 -= dg2 * y_c;
				b2 -= db2 * y_c;
				r3 -= dr3 * y_c;
				g3 -= dg3 * y_c;
				b3 -= db3 * y_c;
				z_c -= depth_increment * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			r1 <<= 16;
			g1 <<= 16;
			b1 <<= 16;
			if (y_a < 0) {
				x_a -= a_to_b * y_a;
				r1 -= dr1 * y_a;
				g1 -= dg1 * y_a;
				b1 -= db1 * y_a;
				y_a = 0;
			}
			if (b_to_c < c_to_a) {
				y_b -= y_a;
				y_a -= y_c;
				for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
					drawShadedScanline(Rasterizer2D.pixels, y_c, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3, z_c,
							depth_slope);
					x_b += b_to_c;
					x_c += c_to_a;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					r3 += dr3;
					g3 += dg3;
					b3 += db3;
					z_c += depth_increment;
				}
				while (--y_b >= 0) {
					drawShadedScanline(Rasterizer2D.pixels, y_c, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1, z_c,
							depth_slope);
					x_b += b_to_c;
					x_a += a_to_b;
					r2 += dr2;
					g2 += dg2;
					b2 += db2;
					r1 += dr1;
					g1 += dg1;
					b1 += db1;
					y_c += Rasterizer2D.width;
					z_c += depth_increment;
				}
				return;
			}
			y_b -= y_a;
			y_a -= y_c;
			for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
				drawShadedScanline(Rasterizer2D.pixels, y_c, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2, z_c, depth_slope);
				x_b += b_to_c;
				x_c += c_to_a;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				r3 += dr3;
				g3 += dg3;
				b3 += db3;
				z_c += depth_increment;
			}
			while (--y_b >= 0) {
				drawShadedScanline(Rasterizer2D.pixels, y_c, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2, z_c, depth_slope);
				x_b += b_to_c;
				x_a += a_to_b;
				r2 += dr2;
				g2 += dg2;
				b2 += db2;
				r1 += dr1;
				g1 += dg1;
				b1 += db1;
				z_c += depth_increment;
				y_c += Rasterizer2D.width;
			}
			return;
		}
		x_a = x_c <<= 16;
		r1 = r3 <<= 16;
		g1 = g3 <<= 16;
		b1 = b3 <<= 16;
		if (y_c < 0) {
			x_a -= b_to_c * y_c;
			x_c -= c_to_a * y_c;
			r1 -= dr2 * y_c;
			g1 -= dg2 * y_c;
			b1 -= db2 * y_c;
			r3 -= dr3 * y_c;
			g3 -= dg3 * y_c;
			b3 -= db3 * y_c;
			z_c -= depth_increment * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		r2 <<= 16;
		g2 <<= 16;
		b2 <<= 16;
		if (y_b < 0) {
			x_b -= a_to_b * y_b;
			r2 -= dr1 * y_b;
			g2 -= dg1 * y_b;
			b2 -= db1 * y_b;
			y_b = 0;
		}
		if (b_to_c < c_to_a) {
			y_a -= y_b;
			y_b -= y_c;
			for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
				drawShadedScanline(Rasterizer2D.pixels, y_c, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3, z_c, depth_slope);
				x_a += b_to_c;
				x_c += c_to_a;
				r1 += dr2;
				g1 += dg2;
				b1 += db2;
				r3 += dr3;
				g3 += dg3;
				b3 += db3;
				z_c += depth_increment;
			}
			while (--y_a >= 0) {
				drawShadedScanline(Rasterizer2D.pixels, y_c, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3, z_c, depth_slope);
				x_b += a_to_b;
				x_c += c_to_a;
				r2 += dr1;
				g2 += dg1;
				b2 += db1;
				r3 += dr3;
				g3 += dg3;
				b3 += db3;
				z_c += depth_increment;
				y_c += Rasterizer2D.width;
			}
			return;
		}
		y_a -= y_b;
		y_b -= y_c;
		for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
			drawShadedScanline(Rasterizer2D.pixels, y_c, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1, z_c, depth_slope);
			x_a += b_to_c;
			x_c += c_to_a;
			r1 += dr2;
			g1 += dg2;
			b1 += db2;
			r3 += dr3;
			g3 += dg3;
			b3 += db3;
			z_c += depth_increment;
		}
		while (--y_a >= 0) {
			drawShadedScanline(Rasterizer2D.pixels, y_c, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2, z_c, depth_slope);
			x_b += a_to_b;
			x_c += c_to_a;
			r2 += dr1;
			g2 += dg1;
			b2 += db1;
			r3 += dr3;
			g3 += dg3;
			b3 += db3;
			y_c += Rasterizer2D.width;
			z_c += depth_increment;
		}
	}

	public static void drawShadedScanline(int[] dest, int offset, int x1, int x2, int r1, int g1, int b1, int r2, int g2, int b2, float depth, float depth_slope) {
		int n = x2 - x1;
		if (n <= 0) {
			return;
		}
		r2 = (r2 - r1) / n;
		g2 = (g2 - g1) / n;
		b2 = (b2 - b1) / n;
		if (textureOutOfDrawingBounds) {
			if (x2 > Rasterizer2D.lastX) {
				n -= x2 - Rasterizer2D.lastX;
				x2 = Rasterizer2D.lastX;
			}
			if (x1 < 0) {
				n = x2;
				r1 -= x1 * r2;
				g1 -= x1 * g2;
				b1 -= x1 * b2;
				x1 = 0;
			}
		}
		if (x1 < x2) {
			offset += x1;
			depth += depth_slope * (float) x1;
			if (alpha == 0) {
				while (--n >= 0) {
					if (true) {
						dest[offset] = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
						Rasterizer2D.depthBuffer[offset] = depth;
					}
					depth += depth_slope;
					r1 += r2;
					g1 += g2;
					b1 += b2;
					offset++;
				}
			} else {
				final int a1 = alpha;
				final int a2 = 256 - alpha;
				int rgb;
				int dst;
				while (--n >= 0) {
					rgb = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
					rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
					dst = dest[offset];
					if (true) {
						dest[offset] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff)
								+ ((dst & 0xff00) * a1 >> 8 & 0xff00);
						Rasterizer2D.depthBuffer[offset] = depth;
					}
					depth += depth_slope;
					r1 += r2;
					g1 += g2;
					b1 += b2;
					offset++;
				}
			}
		}
	}

	public static void drawFlatTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int k1, float z_a,
			float z_b, float z_c) {
		if (z_a < 0 || z_b < 0 || z_c < 0) {
			return;
		}
		int a_to_b = 0;
		if (y_b != y_a) {
			a_to_b = (x_b - x_a << 16) / (y_b - y_a);
		}
		int b_to_c = 0;
		if (y_c != y_b) {
			b_to_c = (x_c - x_b << 16) / (y_c - y_b);
		}
		int c_to_a = 0;
		if (y_c != y_a) {
			c_to_a = (x_a - x_c << 16) / (y_a - y_c);
		}
		float b_aX = x_b - x_a;
		float b_aY = y_b - y_a;
		float c_aX = x_c - x_a;
		float c_aY = y_c - y_a;
		float b_aZ = z_b - z_a;
		float c_aZ = z_c - z_a;

		float div = b_aX * c_aY - c_aX * b_aY;
		float depth_slope = (b_aZ * c_aY - c_aZ * b_aY) / div;
		float depth_increment = (c_aZ * b_aX - b_aZ * c_aX) / div;
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= Rasterizer2D.bottomY)
				return;
			if (y_b > Rasterizer2D.bottomY)
				y_b = Rasterizer2D.bottomY;
			if (y_c > Rasterizer2D.bottomY)
				y_c = Rasterizer2D.bottomY;
			z_a = z_a - depth_slope * x_a + depth_slope;
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				if (y_a < 0) {
					x_c -= c_to_a * y_a;
					x_a -= a_to_b * y_a;
					z_a -= depth_increment * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				if (y_b < 0) {
					x_b -= b_to_c * y_b;
					y_b = 0;
				}
				if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
					y_c -= y_b;
					y_b -= y_a;
					for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_c >> 16, x_a >> 16, z_a, depth_slope);
						x_c += c_to_a;
						x_a += a_to_b;
						z_a += depth_increment;
					}

					while (--y_c >= 0) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_c >> 16, x_b >> 16, z_a, depth_slope);
						x_c += c_to_a;
						x_b += b_to_c;
						y_a += Rasterizer2D.width;
						z_a += depth_increment;
					}
					return;
				}
				y_c -= y_b;
				y_b -= y_a;
				for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_a >> 16, x_c >> 16, z_a, depth_slope);
					x_c += c_to_a;
					x_a += a_to_b;
					z_a += depth_increment;
				}

				while (--y_c >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_b >> 16, x_c >> 16, z_a, depth_slope);
					x_c += c_to_a;
					x_b += b_to_c;
					y_a += Rasterizer2D.width;
					z_a += depth_increment;
				}
				return;
			}
			x_b = x_a <<= 16;
			if (y_a < 0) {
				x_b -= c_to_a * y_a;
				x_a -= a_to_b * y_a;
				z_a -= depth_increment * y_a;
				y_a = 0;

			}
			x_c <<= 16;
			if (y_c < 0) {
				x_c -= b_to_c * y_c;
				y_c = 0;
			}
			if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
				y_b -= y_c;
				y_c -= y_a;
				for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_b >> 16, x_a >> 16, z_a, depth_slope);
					z_a += depth_increment;
					x_b += c_to_a;
					x_a += a_to_b;
				}

				while (--y_b >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_c >> 16, x_a >> 16, z_a, depth_slope);
					z_a += depth_increment;
					x_c += b_to_c;
					x_a += a_to_b;
					y_a += Rasterizer2D.width;
				}
				return;
			}
			y_b -= y_c;
			y_c -= y_a;
			for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_a >> 16, x_b >> 16, z_a, depth_slope);
				z_a += depth_increment;
				x_b += c_to_a;
				x_a += a_to_b;
			}

			while (--y_b >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_a >> 16, x_c >> 16, z_a, depth_slope);
				z_a += depth_increment;
				x_c += b_to_c;
				x_a += a_to_b;
				y_a += Rasterizer2D.width;
			}
			return;
		}
		if (y_b <= y_c) {
			if (y_b >= Rasterizer2D.bottomY)
				return;
			if (y_c > Rasterizer2D.bottomY)
				y_c = Rasterizer2D.bottomY;
			if (y_a > Rasterizer2D.bottomY)
				y_a = Rasterizer2D.bottomY;
			z_b = z_b - depth_slope * x_b + depth_slope;
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				if (y_b < 0) {
					x_a -= a_to_b * y_b;
					x_b -= b_to_c * y_b;
					z_b -= depth_increment * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				if (y_c < 0) {
					x_c -= c_to_a * y_c;
					y_c = 0;
				}
				if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
					y_a -= y_c;
					y_c -= y_b;
					for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_a >> 16, x_b >> 16, z_b, depth_slope);
						z_b += depth_increment;
						x_a += a_to_b;
						x_b += b_to_c;
					}

					while (--y_a >= 0) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_a >> 16, x_c >> 16, z_b, depth_slope);
						z_b += depth_increment;
						x_a += a_to_b;
						x_c += c_to_a;
						y_b += Rasterizer2D.width;
					}
					return;
				}
				y_a -= y_c;
				y_c -= y_b;
				for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_b >> 16, x_a >> 16, z_b, depth_slope);
					z_b += depth_increment;
					x_a += a_to_b;
					x_b += b_to_c;
				}

				while (--y_a >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_c >> 16, x_a >> 16, z_b, depth_slope);
					z_b += depth_increment;
					x_a += a_to_b;
					x_c += c_to_a;
					y_b += Rasterizer2D.width;
				}
				return;
			}
			x_c = x_b <<= 16;
			if (y_b < 0) {
				x_c -= a_to_b * y_b;
				x_b -= b_to_c * y_b;
				z_b -= depth_increment * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			if (y_a < 0) {
				x_a -= c_to_a * y_a;
				y_a = 0;
			}
			if (a_to_b < b_to_c) {
				y_c -= y_a;
				y_a -= y_b;
				for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_c >> 16, x_b >> 16, z_b, depth_slope);
					z_b += depth_increment;
					x_c += a_to_b;
					x_b += b_to_c;
				}

				while (--y_c >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_a >> 16, x_b >> 16, z_b, depth_slope);
					z_b += depth_increment;
					x_a += c_to_a;
					x_b += b_to_c;
					y_b += Rasterizer2D.width;
				}
				return;
			}
			y_c -= y_a;
			y_a -= y_b;
			for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_b >> 16, x_c >> 16, z_b, depth_slope);
				z_b += depth_increment;
				x_c += a_to_b;
				x_b += b_to_c;
			}

			while (--y_c >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_b >> 16, x_a >> 16, z_b, depth_slope);
				z_b += depth_increment;
				x_a += c_to_a;
				x_b += b_to_c;
				y_b += Rasterizer2D.width;
			}
			return;
		}
		if (y_c >= Rasterizer2D.bottomY)
			return;
		if (y_a > Rasterizer2D.bottomY)
			y_a = Rasterizer2D.bottomY;
		if (y_b > Rasterizer2D.bottomY)
			y_b = Rasterizer2D.bottomY;
		z_c = z_c - depth_slope * x_c + depth_slope;
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			if (y_c < 0) {
				x_b -= b_to_c * y_c;
				x_c -= c_to_a * y_c;
				z_c -= depth_increment * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			if (y_a < 0) {
				x_a -= a_to_b * y_a;
				y_a = 0;
			}
			if (b_to_c < c_to_a) {
				y_b -= y_a;
				y_a -= y_c;
				for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_b >> 16, x_c >> 16, z_c, depth_slope);
					z_c += depth_increment;
					x_b += b_to_c;
					x_c += c_to_a;
				}

				while (--y_b >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_b >> 16, x_a >> 16, z_c, depth_slope);
					z_c += depth_increment;
					x_b += b_to_c;
					x_a += a_to_b;
					y_c += Rasterizer2D.width;
				}
				return;
			}
			y_b -= y_a;
			y_a -= y_c;
			for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_c >> 16, x_b >> 16, z_c, depth_slope);
				z_c += depth_increment;
				x_b += b_to_c;
				x_c += c_to_a;
			}

			while (--y_b >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_a >> 16, x_b >> 16, z_c, depth_slope);
				z_c += depth_increment;
				x_b += b_to_c;
				x_a += a_to_b;
				y_c += Rasterizer2D.width;
			}
			return;
		}
		x_a = x_c <<= 16;
		if (y_c < 0) {
			x_a -= b_to_c * y_c;
			x_c -= c_to_a * y_c;
			z_c -= depth_increment * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		if (y_b < 0) {
			x_b -= a_to_b * y_b;
			y_b = 0;
		}
		if (b_to_c < c_to_a) {
			y_a -= y_b;
			y_b -= y_c;
			for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_a >> 16, x_c >> 16, z_c, depth_slope);
				z_c += depth_increment;
				x_a += b_to_c;
				x_c += c_to_a;
			}

			while (--y_a >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_b >> 16, x_c >> 16, z_c, depth_slope);
				z_c += depth_increment;
				x_b += a_to_b;
				x_c += c_to_a;
				y_c += Rasterizer2D.width;
			}
			return;
		}
		y_a -= y_b;
		y_b -= y_c;
		for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
			drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_c >> 16, x_a >> 16, z_c, depth_slope);
			z_c += depth_increment;
			x_a += b_to_c;
			x_c += c_to_a;
		}

		while (--y_a >= 0) {
			drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_c >> 16, x_b >> 16, z_c, depth_slope);
			z_c += depth_increment;
			x_b += a_to_b;
			x_c += c_to_a;
			y_c += Rasterizer2D.width;
		}
	}

	private static void drawFlatTexturedScanline(int dest[], int dest_off, int loops, int start_x, int end_x,
			float depth, float depth_slope) {
		int rgb;
		if (textureOutOfDrawingBounds) {
			if (end_x > Rasterizer2D.lastX)
				end_x = Rasterizer2D.lastX;
			if (start_x < 0)
				start_x = 0;
		}
		if (start_x >= end_x)
			return;
		dest_off += start_x;
		rgb = end_x - start_x >> 2;
		depth += depth_slope * (float) start_x;
		if (alpha == 0) {
			while (--rgb >= 0) {
				for (int i = 0; i < 4; i++) {
					if (true) {
						dest[dest_off] = loops;
						Rasterizer2D.depthBuffer[dest_off] = depth;
					}
					dest_off++;
					depth += depth_slope;
				}
			}
			for (rgb = end_x - start_x & 3; --rgb >= 0;) {
				if (true) {
					dest[dest_off] = loops;
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				dest_off++;
				depth += depth_slope;
			}
			return;
		}
		int dest_alpha = alpha;
		int src_alpha = 256 - alpha;
		loops = ((loops & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((loops & 0xff00) * src_alpha >> 8 & 0xff00);
		while (--rgb >= 0) {
			for (int i = 0; i < 4; i++) {
				if (true) {
					dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff)
							+ ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				dest_off++;
				depth += depth_slope;
			}
		}
		for (rgb = end_x - start_x & 3; --rgb >= 0;) {
			if (true) {
				dest[dest_off] = loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff)
						+ ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00);
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			dest_off++;
			depth += depth_slope;
		}
	}

	public static void drawTexturedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int k1, int l1,
			int i2, int Px, int Mx, int Nx, int Pz, int Mz, int Nz, int Py, int My, int Ny, int k4, float z_a,
			float z_b, float z_c) {
		if (z_a < 0 || z_b < 0 || z_c < 0)
			return;
		int texture[] = getTexturePixels(k4);
		aBoolean1463 = !textureIsTransparant[k4];
		Mx = Px - Mx;
		Mz = Pz - Mz;
		My = Py - My;
		Nx -= Px;
		Nz -= Pz;
		Ny -= Py;
		int Oa = Nx * Pz - Nz * Px << (SceneGraph.viewDistance == 9 ? 14 : 15);
		int Ha = Nz * Py - Ny * Pz << 8;
		int Va = Ny * Px - Nx * Py << 5;
		int Ob = Mx * Pz - Mz * Px << (SceneGraph.viewDistance == 9 ? 14 : 15);
		int Hb = Mz * Py - My * Pz << 8;
		int Vb = My * Px - Mx * Py << 5;
		int Oc = Mz * Nx - Mx * Nz << (SceneGraph.viewDistance == 9 ? 14 : 15);
		int Hc = My * Nz - Mz * Ny << 8;
		int Vc = Mx * Ny - My * Nx << 5;
		int a_to_b = 0;
		int grad_a_off = 0;
		if (y_b != y_a) {
			a_to_b = (x_b - x_a << 16) / (y_b - y_a);
			grad_a_off = (l1 - k1 << 16) / (y_b - y_a);
		}
		int b_to_c = 0;
		int grad_b_off = 0;
		if (y_c != y_b) {
			b_to_c = (x_c - x_b << 16) / (y_c - y_b);
			grad_b_off = (i2 - l1 << 16) / (y_c - y_b);
		}
		int c_to_a = 0;
		int grad_c_off = 0;
		if (y_c != y_a) {
			c_to_a = (x_a - x_c << 16) / (y_a - y_c);
			grad_c_off = (k1 - i2 << 16) / (y_a - y_c);
		}
		float b_aX = x_b - x_a;
		float b_aY = y_b - y_a;
		float c_aX = x_c - x_a;
		float c_aY = y_c - y_a;
		float b_aZ = z_b - z_a;
		float c_aZ = z_c - z_a;

		float div = b_aX * c_aY - c_aX * b_aY;
		float depth_slope = (b_aZ * c_aY - c_aZ * b_aY) / div;
		float depth_increment = (c_aZ * b_aX - b_aZ * c_aX) / div;
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= Rasterizer2D.bottomY)
				return;
			if (y_b > Rasterizer2D.bottomY)
				y_b = Rasterizer2D.bottomY;
			if (y_c > Rasterizer2D.bottomY)
				y_c = Rasterizer2D.bottomY;
			z_a = z_a - depth_slope * x_a + depth_slope;
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				i2 = k1 <<= 16;
				if (y_a < 0) {
					x_c -= c_to_a * y_a;
					x_a -= a_to_b * y_a;
					z_a -= depth_increment * y_a;
					i2 -= grad_c_off * y_a;
					k1 -= grad_a_off * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				l1 <<= 16;
				if (y_b < 0) {
					x_b -= b_to_c * y_b;
					l1 -= grad_b_off * y_b;
					y_b = 0;
				}
				int k8 = y_a - originViewY;
				Oa += Va * k8;
				Ob += Vb * k8;
				Oc += Vc * k8;
				if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
					y_c -= y_b;
					y_b -= y_a;
					y_a = scanOffsets[y_a];
					while (--y_b >= 0) {
						drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_c >> 16, x_a >> 16, i2 >> 8, k1 >> 8, Oa,
								Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
						x_c += c_to_a;
						x_a += a_to_b;
						z_a += depth_increment;
						i2 += grad_c_off;
						k1 += grad_a_off;
						y_a += Rasterizer2D.width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_c >= 0) {
						drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_c >> 16, x_b >> 16, i2 >> 8, l1 >> 8, Oa,
								Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
						x_c += c_to_a;
						x_b += b_to_c;
						z_a += depth_increment;
						i2 += grad_c_off;
						l1 += grad_b_off;
						y_a += Rasterizer2D.width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return;
				}
				y_c -= y_b;
				y_b -= y_a;
				y_a = scanOffsets[y_a];
				while (--y_b >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_a >> 16, x_c >> 16, k1 >> 8, i2 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_a, depth_slope);
					x_c += c_to_a;
					x_a += a_to_b;
					z_a += depth_increment;
					i2 += grad_c_off;
					k1 += grad_a_off;
					y_a += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_c >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_b >> 16, x_c >> 16, l1 >> 8, i2 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_a, depth_slope);
					x_c += c_to_a;
					x_b += b_to_c;
					z_a += depth_increment;
					i2 += grad_c_off;
					l1 += grad_b_off;
					y_a += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return;
			}
			x_b = x_a <<= 16;
			l1 = k1 <<= 16;
			if (y_a < 0) {
				x_b -= c_to_a * y_a;
				x_a -= a_to_b * y_a;
				z_a -= depth_increment * y_a;
				l1 -= grad_c_off * y_a;
				k1 -= grad_a_off * y_a;
				y_a = 0;
			}
			x_c <<= 16;
			i2 <<= 16;
			if (y_c < 0) {
				x_c -= b_to_c * y_c;
				i2 -= grad_b_off * y_c;
				y_c = 0;
			}
			int l8 = y_a - originViewY;
			Oa += Va * l8;
			Ob += Vb * l8;
			Oc += Vc * l8;
			if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
				y_b -= y_c;
				y_c -= y_a;
				y_a = scanOffsets[y_a];
				while (--y_c >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_b >> 16, x_a >> 16, l1 >> 8, k1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_a, depth_slope);
					x_b += c_to_a;
					x_a += a_to_b;
					l1 += grad_c_off;
					k1 += grad_a_off;
					z_a += depth_increment;
					y_a += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_b >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_c >> 16, x_a >> 16, i2 >> 8, k1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_a, depth_slope);
					x_c += b_to_c;
					x_a += a_to_b;
					i2 += grad_b_off;
					k1 += grad_a_off;
					z_a += depth_increment;
					y_a += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return;
			}
			y_b -= y_c;
			y_c -= y_a;
			y_a = scanOffsets[y_a];
			while (--y_c >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_a >> 16, x_b >> 16, k1 >> 8, l1 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_a, depth_slope);
				x_b += c_to_a;
				x_a += a_to_b;
				l1 += grad_c_off;
				k1 += grad_a_off;
				z_a += depth_increment;
				y_a += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_b >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_a >> 16, x_c >> 16, k1 >> 8, i2 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_a, depth_slope);
				x_c += b_to_c;
				x_a += a_to_b;
				i2 += grad_b_off;
				k1 += grad_a_off;
				z_a += depth_increment;
				y_a += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return;
		}
		if (y_b <= y_c) {
			if (y_b >= Rasterizer2D.bottomY)
				return;
			if (y_c > Rasterizer2D.bottomY)
				y_c = Rasterizer2D.bottomY;
			if (y_a > Rasterizer2D.bottomY)
				y_a = Rasterizer2D.bottomY;
			z_b = z_b - depth_slope * x_b + depth_slope;
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				k1 = l1 <<= 16;
				if (y_b < 0) {
					x_a -= a_to_b * y_b;
					x_b -= b_to_c * y_b;
					z_b -= depth_increment * y_b;
					k1 -= grad_a_off * y_b;
					l1 -= grad_b_off * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				i2 <<= 16;
				if (y_c < 0) {
					x_c -= c_to_a * y_c;
					i2 -= grad_c_off * y_c;
					y_c = 0;
				}
				int i9 = y_b - originViewY;
				Oa += Va * i9;
				Ob += Vb * i9;
				Oc += Vc * i9;
				if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
					y_a -= y_c;
					y_c -= y_b;
					y_b = scanOffsets[y_b];
					while (--y_c >= 0) {
						drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_a >> 16, x_b >> 16, k1 >> 8, l1 >> 8, Oa,
								Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
						x_a += a_to_b;
						x_b += b_to_c;
						k1 += grad_a_off;
						l1 += grad_b_off;
						z_b += depth_increment;
						y_b += Rasterizer2D.width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_a >= 0) {
						drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_a >> 16, x_c >> 16, k1 >> 8, i2 >> 8, Oa,
								Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
						x_a += a_to_b;
						x_c += c_to_a;
						k1 += grad_a_off;
						i2 += grad_c_off;
						z_b += depth_increment;
						y_b += Rasterizer2D.width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return;
				}
				y_a -= y_c;
				y_c -= y_b;
				y_b = scanOffsets[y_b];
				while (--y_c >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_b >> 16, x_a >> 16, l1 >> 8, k1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_b, depth_slope);
					x_a += a_to_b;
					x_b += b_to_c;
					k1 += grad_a_off;
					l1 += grad_b_off;
					z_b += depth_increment;
					y_b += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_a >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_c >> 16, x_a >> 16, i2 >> 8, k1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_b, depth_slope);
					x_a += a_to_b;
					x_c += c_to_a;
					k1 += grad_a_off;
					i2 += grad_c_off;
					z_b += depth_increment;
					y_b += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return;
			}
			x_c = x_b <<= 16;
			i2 = l1 <<= 16;
			if (y_b < 0) {
				x_c -= a_to_b * y_b;
				x_b -= b_to_c * y_b;
				z_b -= depth_increment * y_b;
				i2 -= grad_a_off * y_b;
				l1 -= grad_b_off * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			k1 <<= 16;
			if (y_a < 0) {
				x_a -= c_to_a * y_a;
				k1 -= grad_c_off * y_a;
				y_a = 0;
			}
			int j9 = y_b - originViewY;
			Oa += Va * j9;
			Ob += Vb * j9;
			Oc += Vc * j9;
			if (a_to_b < b_to_c) {
				y_c -= y_a;
				y_a -= y_b;
				y_b = scanOffsets[y_b];
				while (--y_a >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_c >> 16, x_b >> 16, i2 >> 8, l1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_b, depth_slope);
					x_c += a_to_b;
					x_b += b_to_c;
					i2 += grad_a_off;
					l1 += grad_b_off;
					z_b += depth_increment;
					y_b += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_c >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_a >> 16, x_b >> 16, k1 >> 8, l1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_b, depth_slope);
					x_a += c_to_a;
					x_b += b_to_c;
					k1 += grad_c_off;
					l1 += grad_b_off;
					z_b += depth_increment;
					y_b += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return;
			}
			y_c -= y_a;
			y_a -= y_b;
			y_b = scanOffsets[y_b];
			while (--y_a >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_b >> 16, x_c >> 16, l1 >> 8, i2 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_b, depth_slope);
				x_c += a_to_b;
				x_b += b_to_c;
				i2 += grad_a_off;
				l1 += grad_b_off;
				z_b += depth_increment;
				y_b += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_c >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_b >> 16, x_a >> 16, l1 >> 8, k1 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_b, depth_slope);
				x_a += c_to_a;
				x_b += b_to_c;
				k1 += grad_c_off;
				l1 += grad_b_off;
				z_b += depth_increment;
				y_b += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return;
		}
		if (y_c >= Rasterizer2D.bottomY)
			return;
		if (y_a > Rasterizer2D.bottomY)
			y_a = Rasterizer2D.bottomY;
		if (y_b > Rasterizer2D.bottomY)
			y_b = Rasterizer2D.bottomY;
		z_c = z_c - depth_slope * x_c + depth_slope;
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			l1 = i2 <<= 16;
			if (y_c < 0) {
				x_b -= b_to_c * y_c;
				x_c -= c_to_a * y_c;
				z_c -= depth_increment * y_c;
				l1 -= grad_b_off * y_c;
				i2 -= grad_c_off * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			k1 <<= 16;
			if (y_a < 0) {
				x_a -= a_to_b * y_a;
				k1 -= grad_a_off * y_a;
				y_a = 0;
			}
			int k9 = y_c - originViewY;
			Oa += Va * k9;
			Ob += Vb * k9;
			Oc += Vc * k9;
			if (b_to_c < c_to_a) {
				y_b -= y_a;
				y_a -= y_c;
				y_c = scanOffsets[y_c];
				while (--y_a >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_b >> 16, x_c >> 16, l1 >> 8, i2 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_c, depth_slope);
					x_b += b_to_c;
					x_c += c_to_a;
					l1 += grad_b_off;
					i2 += grad_c_off;
					z_c += depth_increment;
					y_c += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_b >= 0) {
					drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_b >> 16, x_a >> 16, l1 >> 8, k1 >> 8, Oa, Ob,
							Oc, Ha, Hb, Hc, z_c, depth_slope);
					x_b += b_to_c;
					x_a += a_to_b;
					l1 += grad_b_off;
					k1 += grad_a_off;
					z_c += depth_increment;
					y_c += Rasterizer2D.width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return;
			}
			y_b -= y_a;
			y_a -= y_c;
			y_c = scanOffsets[y_c];
			while (--y_a >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_c >> 16, x_b >> 16, i2 >> 8, l1 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_c, depth_slope);
				x_b += b_to_c;
				x_c += c_to_a;
				l1 += grad_b_off;
				i2 += grad_c_off;
				z_c += depth_increment;
				y_c += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_b >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_a >> 16, x_b >> 16, k1 >> 8, l1 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_c, depth_slope);
				x_b += b_to_c;
				x_a += a_to_b;
				l1 += grad_b_off;
				k1 += grad_a_off;
				z_c += depth_increment;
				y_c += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return;
		}
		x_a = x_c <<= 16;
		k1 = i2 <<= 16;
		if (y_c < 0) {
			x_a -= b_to_c * y_c;
			x_c -= c_to_a * y_c;
			z_c -= depth_increment * y_c;
			k1 -= grad_b_off * y_c;
			i2 -= grad_c_off * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		l1 <<= 16;
		if (y_b < 0) {
			x_b -= a_to_b * y_b;
			l1 -= grad_a_off * y_b;
			y_b = 0;
		}
		int l9 = y_c - originViewY;
		Oa += Va * l9;
		Ob += Vb * l9;
		Oc += Vc * l9;
		if (b_to_c < c_to_a) {
			y_a -= y_b;
			y_b -= y_c;
			y_c = scanOffsets[y_c];
			while (--y_b >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_a >> 16, x_c >> 16, k1 >> 8, i2 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_c, depth_slope);
				x_a += b_to_c;
				x_c += c_to_a;
				k1 += grad_b_off;
				i2 += grad_c_off;
				z_c += depth_increment;
				y_c += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_a >= 0) {
				drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_b >> 16, x_c >> 16, l1 >> 8, i2 >> 8, Oa, Ob, Oc,
						Ha, Hb, Hc, z_c, depth_slope);
				x_b += a_to_b;
				x_c += c_to_a;
				l1 += grad_a_off;
				i2 += grad_c_off;
				z_c += depth_increment;
				y_c += Rasterizer2D.width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return;
		}
		y_a -= y_b;
		y_b -= y_c;
		y_c = scanOffsets[y_c];
		while (--y_b >= 0) {
			drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_c >> 16, x_a >> 16, i2 >> 8, k1 >> 8, Oa, Ob, Oc, Ha,
					Hb, Hc, z_c, depth_slope);
			x_a += b_to_c;
			x_c += c_to_a;
			k1 += grad_b_off;
			i2 += grad_c_off;
			z_c += depth_increment;
			y_c += Rasterizer2D.width;
			Oa += Va;
			Ob += Vb;
			Oc += Vc;
		}
		while (--y_a >= 0) {
			drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_c >> 16, x_b >> 16, i2 >> 8, l1 >> 8, Oa, Ob, Oc, Ha,
					Hb, Hc, z_c, depth_slope);
			x_b += a_to_b;
			x_c += c_to_a;
			l1 += grad_a_off;
			i2 += grad_c_off;
			z_c += depth_increment;
			y_c += Rasterizer2D.width;
			Oa += Va;
			Ob += Vb;
			Oc += Vc;
		}
	}

	public static void drawTexturedScanline(int dest[], int texture[], int dest_off, int start_x, int end_x,
			int shadeValue, int gradient, int l1, int i2, int j2, int k2, int l2, int i3, float depth,
			float depth_slope) {
		int rgb = 0;
		int loops = 0;
		if (start_x >= end_x)
			return;
		int j3;
		int k3;
		if (textureOutOfDrawingBounds) {
			j3 = (gradient - shadeValue) / (end_x - start_x);
			if (end_x > Rasterizer2D.lastX)
				end_x = Rasterizer2D.lastX;
			if (start_x < 0) {
				shadeValue -= start_x * j3;
				start_x = 0;
			}
			if (start_x >= end_x)
				return;
			k3 = end_x - start_x >> 3;
			j3 <<= 12;
			shadeValue <<= 9;
		} else {
			if (end_x - start_x > 7) {
				k3 = end_x - start_x >> 3;
				j3 = (gradient - shadeValue) * anIntArray1468[k3] >> 6;
			} else {
				k3 = 0;
				j3 = 0;
			}
			shadeValue <<= 9;
		}
		dest_off += start_x;
		depth += depth_slope * (float) start_x;
		if (lowMem) {
			int i4 = 0;
			int k4 = 0;
			int k6 = start_x - originViewX;
			l1 += (k2 >> 3) * k6;
			i2 += (l2 >> 3) * k6;
			j2 += (i3 >> 3) * k6;
			int i5 = j2 >> 12;
			if (i5 != 0) {
				rgb = l1 / i5;
				loops = i2 / i5;
				if (rgb < 0)
					rgb = 0;
				else if (rgb > 4032)
					rgb = 4032;
			}
			l1 += k2;
			i2 += l2;
			j2 += i3;
			i5 = j2 >> 12;
			if (i5 != 0) {
				i4 = l1 / i5;
				k4 = i2 / i5;
				if (i4 < 7)
					i4 = 7;
				else if (i4 > 4032)
					i4 = 4032;
			}
			int i7 = i4 - rgb >> 3;
			int k7 = k4 - loops >> 3;
			rgb += (shadeValue & 0x600000) >> 3;
			int i8 = shadeValue >> 23;
			if (aBoolean1463) {
				while (k3-- > 0) {
					for (int i = 0; i < 8; i++) {
						if (true) {
							dest[dest_off] = texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8;
							Rasterizer2D.depthBuffer[dest_off] = depth;
						}
						dest_off++;
						depth += depth_slope;
						rgb += i7;
						loops += k7;
					}
					rgb = i4;
					loops = k4;
					l1 += k2;
					i2 += l2;
					j2 += i3;
					int j5 = j2 >> 12;
					if (j5 != 0) {
						i4 = l1 / j5;
						k4 = i2 / j5;
						if (i4 < 7)
							i4 = 7;
						else if (i4 > 4032)
							i4 = 4032;
					}
					i7 = i4 - rgb >> 3;
					k7 = k4 - loops >> 3;
					shadeValue += j3;
					rgb += (shadeValue & 0x600000) >> 3;
					i8 = shadeValue >> 23;
				}
				for (k3 = end_x - start_x & 7; k3-- > 0;) {
					if (true) {
						dest[dest_off] = texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8;
						Rasterizer2D.depthBuffer[dest_off] = depth;
					}
					dest_off++;
					depth += depth_slope;
					rgb += i7;
					loops += k7;
				}

				return;
			}
			while (k3-- > 0) {
				int k8;
				for (int i = 0; i < 8; i++) {
					if ((k8 = texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8) != 0) {
						dest[dest_off] = k8;
						Rasterizer2D.depthBuffer[dest_off] = depth;
					}
					dest_off++;
					depth += depth_slope;
					rgb += i7;
					loops += k7;
				}

				rgb = i4;
				loops = k4;
				l1 += k2;
				i2 += l2;
				j2 += i3;
				int k5 = j2 >> 12;
				if (k5 != 0) {
					i4 = l1 / k5;
					k4 = i2 / k5;
					if (i4 < 7)
						i4 = 7;
					else if (i4 > 4032)
						i4 = 4032;
				}
				i7 = i4 - rgb >> 3;
				k7 = k4 - loops >> 3;
				shadeValue += j3;
				rgb += (shadeValue & 0x600000) >> 3;
				i8 = shadeValue >> 23;
			}
			for (k3 = end_x - start_x & 7; k3-- > 0;) {
				int l8;
				if ((l8 = texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8) != 0) {
					dest[dest_off] = l8;
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				dest_off++;
				depth += depth_slope;
				rgb += i7;
				loops += k7;
			}

			return;
		}
		int j4 = 0;
		int l4 = 0;
		int l6 = start_x - originViewX;
		l1 += (k2 >> 3) * l6;
		i2 += (l2 >> 3) * l6;
		j2 += (i3 >> 3) * l6;
		int l5 = j2 >> 14;
		if (l5 != 0) {
			rgb = l1 / l5;
			loops = i2 / l5;
			if (rgb < 0)
				rgb = 0;
			else if (rgb > 16256)
				rgb = 16256;
		}
		l1 += k2;
		i2 += l2;
		j2 += i3;
		l5 = j2 >> 14;
		if (l5 != 0) {
			j4 = l1 / l5;
			l4 = i2 / l5;
			if (j4 < 7)
				j4 = 7;
			else if (j4 > 16256)
				j4 = 16256;
		}
		int j7 = j4 - rgb >> 3;
		int l7 = l4 - loops >> 3;
		rgb += shadeValue & 0x600000;
		int j8 = shadeValue >> 23;
		if (aBoolean1463) {
			while (k3-- > 0) {
				for (int i = 0; i < 8; i++) {
					if (true) {
						dest[dest_off] = texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8;
						Rasterizer2D.depthBuffer[dest_off] = depth;
					}
					depth += depth_slope;
					dest_off++;
					rgb += j7;
					loops += l7;
				}
				rgb = j4;
				loops = l4;
				l1 += k2;
				i2 += l2;
				j2 += i3;
				int i6 = j2 >> 14;
				if (i6 != 0) {
					j4 = l1 / i6;
					l4 = i2 / i6;
					if (j4 < 7)
						j4 = 7;
					else if (j4 > 16256)
						j4 = 16256;
				}
				j7 = j4 - rgb >> 3;
				l7 = l4 - loops >> 3;
				shadeValue += j3;
				rgb += shadeValue & 0x600000;
				j8 = shadeValue >> 23;
			}
			for (k3 = end_x - start_x & 7; k3-- > 0;) {
				if (true) {
					dest[dest_off] = texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8;
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				dest_off++;
				depth += depth_slope;
				rgb += j7;
				loops += l7;
			}

			return;
		}
		while (k3-- > 0) {
			int i9;
			for (int i = 0; i < 8; i++) {
				if ((i9 = texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8) != 0) {
					dest[dest_off] = i9;
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				dest_off++;
				depth += depth_slope;
				rgb += j7;
				loops += l7;
			}
			rgb = j4;
			loops = l4;
			l1 += k2;
			i2 += l2;
			j2 += i3;
			int j6 = j2 >> 14;
			if (j6 != 0) {
				j4 = l1 / j6;
				l4 = i2 / j6;
				if (j4 < 7)
					j4 = 7;
				else if (j4 > 16256)
					j4 = 16256;
			}
			j7 = j4 - rgb >> 3;
			l7 = l4 - loops >> 3;
			shadeValue += j3;
			rgb += shadeValue & 0x600000;
			j8 = shadeValue >> 23;
		}
		for (int l3 = end_x - start_x & 7; l3-- > 0;) {
			int j9;
			if ((j9 = texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8) != 0) {
				dest[dest_off] = j9;
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			depth += depth_slope;
			dest_off++;
			rgb += j7;
			loops += l7;
		}
	}

	public static void drawDepthTriangle(int x_a, int x_b, int x_c, int y_a, int y_b, int y_c, float z_a, float z_b,
			float z_c) {
		int a_to_b = 0;
		if (y_b != y_a) {
			a_to_b = (x_b - x_a << 16) / (y_b - y_a);
		}
		int b_to_c = 0;
		if (y_c != y_b) {
			b_to_c = (x_c - x_b << 16) / (y_c - y_b);
		}
		int c_to_a = 0;
		if (y_c != y_a) {
			c_to_a = (x_a - x_c << 16) / (y_a - y_c);
		}

		float b_aX = x_b - x_a;
		float b_aY = y_b - y_a;
		float c_aX = x_c - x_a;
		float c_aY = y_c - y_a;
		float b_aZ = z_b - z_a;
		float c_aZ = z_c - z_a;

		float div = b_aX * c_aY - c_aX * b_aY;
		float depth_slope = (b_aZ * c_aY - c_aZ * b_aY) / div;
		float depth_increment = (c_aZ * b_aX - b_aZ * c_aX) / div;
		if (y_a <= y_b && y_a <= y_c) {
			if (y_a < Rasterizer2D.bottomY) {
				if (y_b > Rasterizer2D.bottomY)
					y_b = Rasterizer2D.bottomY;
				if (y_c > Rasterizer2D.bottomY)
					y_c = Rasterizer2D.bottomY;
				z_a = z_a - depth_slope * x_a + depth_slope;
				if (y_b < y_c) {
					x_c = x_a <<= 16;
					if (y_a < 0) {
						x_c -= c_to_a * y_a;
						x_a -= a_to_b * y_a;
						z_a -= depth_increment * y_a;
						y_a = 0;
					}
					x_b <<= 16;
					if (y_b < 0) {
						x_b -= b_to_c * y_b;
						y_b = 0;
					}
					if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
						y_c -= y_b;
						y_b -= y_a;
						y_a = scanOffsets[y_a];
						while (--y_b >= 0) {
							drawDepthTriangleScanline(y_a, x_c >> 16, x_a >> 16, z_a, depth_slope);
							x_c += c_to_a;
							x_a += a_to_b;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_a, x_c >> 16, x_b >> 16, z_a, depth_slope);
							x_c += c_to_a;
							x_b += b_to_c;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
					} else {
						y_c -= y_b;
						y_b -= y_a;
						y_a = scanOffsets[y_a];
						while (--y_b >= 0) {
							drawDepthTriangleScanline(y_a, x_a >> 16, x_c >> 16, z_a, depth_slope);
							x_c += c_to_a;
							x_a += a_to_b;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_a, x_b >> 16, x_c >> 16, z_a, depth_slope);
							x_c += c_to_a;
							x_b += b_to_c;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
					}
				} else {
					x_b = x_a <<= 16;
					if (y_a < 0) {
						x_b -= c_to_a * y_a;
						x_a -= a_to_b * y_a;
						z_a -= depth_increment * y_a;
						y_a = 0;
					}
					x_c <<= 16;
					if (y_c < 0) {
						x_c -= b_to_c * y_c;
						y_c = 0;
					}
					if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
						y_b -= y_c;
						y_c -= y_a;
						y_a = scanOffsets[y_a];
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_a, x_b >> 16, x_a >> 16, z_a, depth_slope);
							x_b += c_to_a;
							x_a += a_to_b;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
						while (--y_b >= 0) {
							drawDepthTriangleScanline(y_a, x_c >> 16, x_a >> 16, z_a, depth_slope);
							x_c += b_to_c;
							x_a += a_to_b;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
					} else {
						y_b -= y_c;
						y_c -= y_a;
						y_a = scanOffsets[y_a];
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_a, x_a >> 16, x_b >> 16, z_a, depth_slope);
							x_b += c_to_a;
							x_a += a_to_b;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
						while (--y_b >= 0) {
							drawDepthTriangleScanline(y_a, x_a >> 16, x_c >> 16, z_a, depth_slope);
							x_c += b_to_c;
							x_a += a_to_b;
							z_a += depth_increment;
							y_a += Rasterizer2D.width;
						}
					}
				}
			}
		} else if (y_b <= y_c) {
			if (y_b < Rasterizer2D.bottomY) {
				if (y_c > Rasterizer2D.bottomY)
					y_c = Rasterizer2D.bottomY;
				if (y_a > Rasterizer2D.bottomY)
					y_a = Rasterizer2D.bottomY;
				z_b = z_b - depth_slope * x_b + depth_slope;
				if (y_c < y_a) {
					x_a = x_b <<= 16;
					if (y_b < 0) {
						x_a -= a_to_b * y_b;
						x_b -= b_to_c * y_b;
						z_b -= depth_increment * y_b;
						y_b = 0;
					}
					x_c <<= 16;
					if (y_c < 0) {
						x_c -= c_to_a * y_c;
						y_c = 0;
					}
					if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
						y_a -= y_c;
						y_c -= y_b;
						y_b = scanOffsets[y_b];
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_b, x_a >> 16, x_b >> 16, z_b, depth_slope);
							x_a += a_to_b;
							x_b += b_to_c;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
						while (--y_a >= 0) {
							drawDepthTriangleScanline(y_b, x_a >> 16, x_c >> 16, z_b, depth_slope);
							x_a += a_to_b;
							x_c += c_to_a;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
					} else {
						y_a -= y_c;
						y_c -= y_b;
						y_b = scanOffsets[y_b];
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_b, x_b >> 16, x_a >> 16, z_b, depth_slope);
							x_a += a_to_b;
							x_b += b_to_c;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
						while (--y_a >= 0) {
							drawDepthTriangleScanline(y_b, x_c >> 16, x_a >> 16, z_b, depth_slope);
							x_a += a_to_b;
							x_c += c_to_a;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
					}
				} else {
					x_c = x_b <<= 16;
					if (y_b < 0) {
						x_c -= a_to_b * y_b;
						x_b -= b_to_c * y_b;
						z_b -= depth_increment * y_b;
						y_b = 0;
					}
					x_a <<= 16;
					if (y_a < 0) {
						x_a -= c_to_a * y_a;
						y_a = 0;
					}
					if (a_to_b < b_to_c) {
						y_c -= y_a;
						y_a -= y_b;
						y_b = scanOffsets[y_b];
						while (--y_a >= 0) {
							drawDepthTriangleScanline(y_b, x_c >> 16, x_b >> 16, z_b, depth_slope);
							x_c += a_to_b;
							x_b += b_to_c;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_b, x_a >> 16, x_b >> 16, z_b, depth_slope);
							x_a += c_to_a;
							x_b += b_to_c;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
					} else {
						y_c -= y_a;
						y_a -= y_b;
						y_b = scanOffsets[y_b];
						while (--y_a >= 0) {
							drawDepthTriangleScanline(y_b, x_b >> 16, x_c >> 16, z_b, depth_slope);
							x_c += a_to_b;
							x_b += b_to_c;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
						while (--y_c >= 0) {
							drawDepthTriangleScanline(y_b, x_b >> 16, x_a >> 16, z_b, depth_slope);
							x_a += c_to_a;
							x_b += b_to_c;
							z_b += depth_increment;
							y_b += Rasterizer2D.width;
						}
					}
				}
			}
		} else if (y_c < Rasterizer2D.bottomY) {
			if (y_a > Rasterizer2D.bottomY)
				y_a = Rasterizer2D.bottomY;
			if (y_b > Rasterizer2D.bottomY)
				y_b = Rasterizer2D.bottomY;
			z_c = z_c - depth_slope * x_c + depth_slope;
			if (y_a < y_b) {
				x_b = x_c <<= 16;
				if (y_c < 0) {
					x_b -= b_to_c * y_c;
					x_c -= c_to_a * y_c;
					z_c -= depth_increment * y_c;
					y_c = 0;
				}
				x_a <<= 16;
				if (y_a < 0) {
					x_a -= a_to_b * y_a;
					y_a = 0;
				}
				if (b_to_c < c_to_a) {
					y_b -= y_a;
					y_a -= y_c;
					y_c = scanOffsets[y_c];
					while (--y_a >= 0) {
						drawDepthTriangleScanline(y_c, x_b >> 16, x_c >> 16, z_c, depth_slope);
						x_b += b_to_c;
						x_c += c_to_a;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
					while (--y_b >= 0) {
						drawDepthTriangleScanline(y_c, x_b >> 16, x_a >> 16, z_c, depth_slope);
						x_b += b_to_c;
						x_a += a_to_b;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
				} else {
					y_b -= y_a;
					y_a -= y_c;
					y_c = scanOffsets[y_c];
					while (--y_a >= 0) {
						drawDepthTriangleScanline(y_c, x_c >> 16, x_b >> 16, z_c, depth_slope);
						x_b += b_to_c;
						x_c += c_to_a;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
					while (--y_b >= 0) {
						drawDepthTriangleScanline(y_c, x_a >> 16, x_b >> 16, z_c, depth_slope);
						x_b += b_to_c;
						x_a += a_to_b;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
				}
			} else {
				x_a = x_c <<= 16;
				if (y_c < 0) {
					x_a -= b_to_c * y_c;
					x_c -= c_to_a * y_c;
					z_c -= depth_increment * y_c;
					y_c = 0;
				}
				x_b <<= 16;
				if (y_b < 0) {
					x_b -= a_to_b * y_b;
					y_b = 0;
				}
				if (b_to_c < c_to_a) {
					y_a -= y_b;
					y_b -= y_c;
					y_c = scanOffsets[y_c];
					while (--y_b >= 0) {
						drawDepthTriangleScanline(y_c, x_a >> 16, x_c >> 16, z_c, depth_slope);
						x_a += b_to_c;
						x_c += c_to_a;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
					while (--y_a >= 0) {
						drawDepthTriangleScanline(y_c, x_b >> 16, x_c >> 16, z_c, depth_slope);
						x_b += a_to_b;
						x_c += c_to_a;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
				} else {
					y_a -= y_b;
					y_b -= y_c;
					y_c = scanOffsets[y_c];
					while (--y_b >= 0) {
						drawDepthTriangleScanline(y_c, x_c >> 16, x_a >> 16, z_c, depth_slope);
						x_a += b_to_c;
						x_c += c_to_a;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
					while (--y_a >= 0) {
						drawDepthTriangleScanline(y_c, x_c >> 16, x_b >> 16, z_c, depth_slope);
						x_b += a_to_b;
						x_c += c_to_a;
						z_c += depth_increment;
						y_c += Rasterizer2D.width;
					}
				}
			}
		}
	}

	private static void drawDepthTriangleScanline(int dest_off, int start_x, int end_x, float depth,
			float depth_slope) {
		int dbl = Rasterizer2D.depthBuffer.length;
		if (textureOutOfDrawingBounds) {
			if (end_x > Rasterizer2D.width) {
				end_x = Rasterizer2D.width;
			}
			if (start_x < 0) {
				start_x = 0;
			}
		}
		if (start_x >= end_x) {
			return;
		}
		dest_off += start_x - 1;
		int loops = end_x - start_x >> 2;
		depth += depth_slope * (float) start_x;
		if (alpha == 0) {
			while (--loops >= 0) {
				dest_off++;
				if (dest_off >= 0 && dest_off < dbl) {
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				depth += depth_slope;
				dest_off++;
				if (dest_off >= 0 && dest_off < dbl) {
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				depth += depth_slope;
				dest_off++;
				if (dest_off >= 0 && dest_off < dbl) {
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				depth += depth_slope;
				dest_off++;
				if (dest_off >= 0 && dest_off < dbl) {
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				depth += depth_slope;
			}
			for (loops = end_x - start_x & 3; --loops >= 0;) {
				dest_off++;
				if (dest_off >= 0 && dest_off < dbl) {
					Rasterizer2D.depthBuffer[dest_off] = depth;
				}
				depth += depth_slope;
			}
			return;
		}
		while (--loops >= 0) {
			dest_off++;
			if (dest_off >= 0 && dest_off < dbl) {
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			depth += depth_slope;
			dest_off++;
			if (dest_off >= 0 && dest_off < dbl) {
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			depth += depth_slope;
			dest_off++;
			if (dest_off >= 0 && dest_off < dbl) {
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			depth += depth_slope;
			dest_off++;
			if (dest_off >= 0 && dest_off < dbl) {
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			depth += depth_slope;
		}
		for (loops = end_x - start_x & 3; --loops >= 0;) {
			dest_off++;
			if (dest_off >= 0 && dest_off < dbl) {
				Rasterizer2D.depthBuffer[dest_off] = depth;
			}
			depth += depth_slope;
		}
	}

	public static boolean lowMem = true;
	public static boolean textureOutOfDrawingBounds;
	private static boolean aBoolean1463;
	public static boolean aBoolean1464 = true;
	public static int alpha;
	public static int originViewX;
	public static int originViewY;	
	private static int[] anIntArray1468;
	public static final int[] anIntArray1469;
	public static int anIntArray1470[];
	public static int COSINE[];
	public static int scanOffsets[];
	private static int textureCount;
	public static IndexedImage textures[] = new IndexedImage[51];
	private static boolean[] textureIsTransparant = new boolean[51];
	private static int[] averageTextureColours = new int[51];
	private static int textureRequestBufferPointer;
	private static int[][] textureRequestPixelBuffer;
	private static int[][] texturesPixelBuffer = new int[51][];
	public static int textureLastUsed[] = new int[51];
	public static int lastTextureRetrievalCount;
	public static int hslToRgb[] = new int[0x10000];
	private static int[][] currentPalette = new int[51][];

	static {
		anIntArray1468 = new int[512];
		anIntArray1469 = new int[2048];
		anIntArray1470 = new int[2048];
		COSINE = new int[2048];
		for (int i = 1; i < 512; i++) {
			anIntArray1468[i] = 32768 / i;
		}
		for (int j = 1; j < 2048; j++) {
			anIntArray1469[j] = 0x10000 / j;
		}
		for (int k = 0; k < 2048; k++) {
			anIntArray1470[k] = (int) (65536D * Math.sin((double) k * 0.0030679614999999999D));
			COSINE[k] = (int) (65536D * Math.cos((double) k * 0.0030679614999999999D));
		}
	}
}