package com.runescape.cache.graphics;
import java.awt.*;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import org.seven.util.FileUtils;

import com.runescape.Client;
import com.runescape.cache.FileArchive;
import com.runescape.draw.Rasterizer2D;
import com.runescape.io.Buffer;
import com.runescape.sign.SignLink;

public final class Sprite extends Rasterizer2D {

	public Sprite(int i, int j) {
		myPixels = new int[i * j];
		myWidth = maxWidth = i;
		myHeight = maxHeight = j;
		drawOffsetX = drawOffsetY = 0;
	}

	public String location = SignLink.findcachedir() + "Sprites/";

	public Sprite(byte data[], Component component) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(data);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			myWidth = image.getWidth(component);
			myHeight = image.getHeight(component);
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
		} catch (Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}

	public Sprite(String img, int width, int height) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(FileUtils.readFile(img));
			myWidth = width;
			myHeight = height;
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}

	public Sprite(String img) {
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(location + img + ".png");
			ImageIcon sprite = new ImageIcon(image);
			myWidth = sprite.getIconWidth();
			myHeight = sprite.getIconHeight();
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}

	public void drawHoverSprite(int x, int y, int offsetX, int offsetY, Sprite hover) {
		this.drawSprite(x, y);
		if (Client.instance.mouseX >= offsetX + x && Client.instance.mouseX <= offsetX + x + this.myWidth 
				&& Client.instance.mouseY >= offsetY + y && Client.instance.mouseY <= offsetY + y + this.myHeight) {
			hover.drawSprite(x, y);
		}
	}

	public void draw24BitSprite(int x, int y) {
		int alpha = 256;
		x += this.drawOffsetX;// offsetX
		y += this.drawOffsetY;// offsetY
		int destOffset = x + y * Rasterizer2D.width;
		int srcOffset = 0;
		int height = this.myHeight;
		int width = this.myWidth;
		int destStep = Rasterizer2D.width - width;
		int srcStep = 0;
		if (y < Rasterizer2D.topY) {
			int trimHeight = Rasterizer2D.topY - y;
			height -= trimHeight;
			y = Rasterizer2D.topY;
			srcOffset += trimHeight * width;
			destOffset += trimHeight * Rasterizer2D.width;
		}
		if (y + height > Rasterizer2D.bottomY) {
			height -= (y + height) - Rasterizer2D.bottomY;
		}
		if (x < Rasterizer2D.leftX) {
			int trimLeft = Rasterizer2D.leftX - x;
			width -= trimLeft;
			x = Rasterizer2D.leftX;
			srcOffset += trimLeft;
			destOffset += trimLeft;
			srcStep += trimLeft;
			destStep += trimLeft;
		}
		if (x + width > Rasterizer2D.bottomX) {
			int trimRight = (x + width) - Rasterizer2D.bottomX;
			width -= trimRight;
			srcStep += trimRight;
			destStep += trimRight;
		}
		if (!((width <= 0) || (height <= 0))) {
			set24BitPixels(width, height, Rasterizer2D.pixels, myPixels, alpha, destOffset, srcOffset, destStep, srcStep);
		}
	}

	public void drawTransparentSprite(int x, int y, int opacity) {
		int k = opacity;// was parameter
		x += drawOffsetX;
		y += drawOffsetY;
		int i1 = x + y * Rasterizer2D.width;
		int j1 = 0;
		int k1 = myHeight;
		int l1 = myWidth;
		int i2 = Rasterizer2D.width - l1;
		int j2 = 0;
		if (y < Rasterizer2D.topY) {
			int k2 = Rasterizer2D.topY - y;
			k1 -= k2;
			y = Rasterizer2D.topY;
			j1 += k2 * l1;
			i1 += k2 * Rasterizer2D.width;
		}
		if (y + k1 > Rasterizer2D.bottomY)
			k1 -= (y + k1) - Rasterizer2D.bottomY;
		if (x < Rasterizer2D.leftX) {
			int l2 = Rasterizer2D.leftX - x;
			l1 -= l2;
			x = Rasterizer2D.leftX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (x + l1 > Rasterizer2D.bottomX) {
			int i3 = (x + l1) - Rasterizer2D.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(l1 <= 0 || k1 <= 0)) {
			method351(j1, l1, Rasterizer2D.pixels, myPixels, j2, k1, i2, k, i1);
		}
	}

	private void set24BitPixels(int width, int height, int destPixels[], int srcPixels[], int srcAlpha, int destOffset, int srcOffset, int destStep, int srcStep) {
		int srcColor;
		int destAlpha;
		for (int loop = -height; loop < 0; loop++) {
			for (int loop2 = -width; loop2 < 0; loop2++) {
				srcAlpha = ((this.myPixels[srcOffset] >> 24) & 255);
				destAlpha = 256 - srcAlpha;
				srcColor = srcPixels[srcOffset++];
				if (srcColor != 0 && srcColor != 0xffffff) {
					int destColor = destPixels[destOffset];
					destPixels[destOffset++] = ((srcColor & 0xff00ff) * srcAlpha + (destColor & 0xff00ff) * destAlpha & 0xff00ff00) + ((srcColor & 0xff00) * srcAlpha + (destColor & 0xff00) * destAlpha & 0xff0000) >> 8;
				} else {
					destOffset++;
				}
			}
			destOffset += destStep;
			srcOffset += srcStep;
		}
	}

	public void setTransparency(int transRed, int transGreen, int transBlue) {
		for (int index = 0; index < myPixels.length; index++)
			if (((myPixels[index] >> 16) & 255) == transRed && ((myPixels[index] >> 8) & 255) == transGreen && (myPixels[index] & 255) == transBlue)
				myPixels[index] = 0;
	}

	public Sprite(FileArchive archive, String name, int i) {		
		Buffer dataBuffer = new Buffer(archive.readFile(name + ".dat"));		
		Buffer indexBuffer = new Buffer(archive.readFile("index.dat"));
		
		indexBuffer.currentPosition = dataBuffer.readUShort();
		
		maxWidth = indexBuffer.readUShort();
		maxHeight = indexBuffer.readUShort();
		int pixelCount = indexBuffer.readUnsignedByte();		
		int raster[] = new int[pixelCount];
		
		for (int pixel = 0; pixel < pixelCount - 1; pixel++) {			
			raster[pixel + 1] = indexBuffer.readTriByte();
			if (raster[pixel + 1] == 0)
				raster[pixel + 1] = 1;
		}

		for (int index = 0; index < i; index++) {			
			indexBuffer.currentPosition += 2;
			dataBuffer.currentPosition += indexBuffer.readUShort() * indexBuffer.readUShort();
			indexBuffer.currentPosition++;
		}

		drawOffsetX = indexBuffer.readUnsignedByte();
		drawOffsetY = indexBuffer.readUnsignedByte();
		myWidth = indexBuffer.readUShort();
		myHeight = indexBuffer.readUShort();
		
		int type = indexBuffer.readUnsignedByte();
		
		int spriteSize = myWidth * myHeight;
		
		myPixels = new int[spriteSize];
		if (type == 0) {
			for (int pixel = 0; pixel < spriteSize; pixel++) {
				myPixels[pixel] = raster[dataBuffer.readUnsignedByte()];
			}
			setTransparency(255, 0, 255);
			return;
		}
		if (type == 1) {
			for (int x = 0; x < myWidth; x++) {					
				for (int y = 0; y < myHeight; y++) {
					myPixels[x + y * myWidth] = raster[dataBuffer.readUnsignedByte()];
				}
			}

		}
		setTransparency(255, 0, 255);
	}

	public void init() {
		Rasterizer2D.initDrawingArea(myHeight, myWidth, myPixels, null);
	}

	public void method344(int i, int j, int k) {
		for (int i1 = 0; i1 < myPixels.length; i1++) {
			int j1 = myPixels[i1];
			if (j1 != 0) {
				int k1 = j1 >> 16 & 0xff;
				k1 += i;
				if (k1 < 1)
					k1 = 1;
				else if (k1 > 255)
					k1 = 255;
				int l1 = j1 >> 8 & 0xff;
				l1 += j;
				if (l1 < 1)
					l1 = 1;
				else if (l1 > 255)
					l1 = 255;
				int i2 = j1 & 0xff;
				i2 += k;
				if (i2 < 1)
					i2 = 1;
				else if (i2 > 255)
					i2 = 255;
				myPixels[i1] = (k1 << 16) + (l1 << 8) + i2;
			}
		}

	}

	public void method345() {
		int ai[] = new int[maxWidth * maxHeight];
		for (int j = 0; j < myHeight; j++) {
			System.arraycopy(myPixels, j * myWidth, ai, j + drawOffsetY * maxWidth + drawOffsetX, myWidth);
		}

		myPixels = ai;
		myWidth = maxWidth;
		myHeight = maxHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void method346(int x, int y) {
		x += drawOffsetX;
		y += drawOffsetY;
		int l = x + y * Rasterizer2D.width;
		int i1 = 0;
		int height = myHeight;
		int width = myWidth;
		int l1 = Rasterizer2D.width - width;
		int i2 = 0;
		if (y < Rasterizer2D.topY) {
			int j2 = Rasterizer2D.topY - y;
			height -= j2;
			y = Rasterizer2D.topY;
			i1 += j2 * width;
			l += j2 * Rasterizer2D.width;
		}
		if (y + height > Rasterizer2D.bottomY)
			height -= (y + height) - Rasterizer2D.bottomY;
		if (x < Rasterizer2D.leftX) {
			int k2 = Rasterizer2D.leftX - x;
			width -= k2;
			x = Rasterizer2D.leftX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (x + width > Rasterizer2D.bottomX) {
			int l2 = (x + width) - Rasterizer2D.bottomX;
			width -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (width <= 0 || height <= 0) {
		} else {
			method347(l, width, height, i2, i1, l1, myPixels, Rasterizer2D.pixels);
		}
	}

	private void method347(int i, int j, int k, int l, int i1, int k1, int ai[], int ai1[]) {
		int l1 = -(j >> 2);
		j = -(j & 3);
		for (int i2 = -k; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
				ai1[i++] = ai[i1++];
			}

			for (int k2 = j; k2 < 0; k2++)
				ai1[i++] = ai[i1++];

			i += k1;
			i1 += l;
		}
	}
	
	public void drawSprite1(int i, int j) {
	      drawSprite1(i, j, 128);
	}

	public void drawSprite1(int i, int j, int k) {
		i += drawOffsetX;
		j += drawOffsetY;
		int i1 = i + j * Rasterizer2D.width;
		int j1 = 0;
		int k1 = myHeight;
		int l1 = myWidth;
		int i2 = Rasterizer2D.width - l1;
		int j2 = 0;
		if (j < Rasterizer2D.topY) {
			int k2 = Rasterizer2D.topY - j;
			k1 -= k2;
			j = Rasterizer2D.topY;
			j1 += k2 * l1;
			i1 += k2 * Rasterizer2D.width;
		}
		if (j + k1 > Rasterizer2D.bottomY)
			k1 -= (j + k1) - Rasterizer2D.bottomY;
		if (i < Rasterizer2D.leftX) {
			int l2 = Rasterizer2D.leftX - i;
			l1 -= l2;
			i = Rasterizer2D.leftX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (i + l1 > Rasterizer2D.bottomX) {
			int i3 = (i + l1) - Rasterizer2D.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(l1 <= 0 || k1 <= 0)) {
			method351(j1, l1, Rasterizer2D.pixels, myPixels, j2, k1, i2, k, i1);
		}
	}

	public void drawSprite(int x, int y)
	{
		x += drawOffsetX;
		y += drawOffsetY;
		int rasterClip = x + y * Rasterizer2D.width;
		int imageClip = 0;
		int height = myHeight;
		int width = myWidth;
		int rasterOffset = Rasterizer2D.width - width;
		int imageOffset = 0;
		if(y < Rasterizer2D.topY)
		{
			int dy = Rasterizer2D.topY - y;
			height -= dy;
			y = Rasterizer2D.topY;
			imageClip += dy * width;
			rasterClip += dy * Rasterizer2D.width;
		}
		if(y + height > Rasterizer2D.bottomY)
			height -= (y + height) - Rasterizer2D.bottomY;
		if(x < Rasterizer2D.leftX)
		{
			int dx = Rasterizer2D.leftX - x;
			width -= dx;
			x = Rasterizer2D.leftX;
			imageClip += dx;
			rasterClip += dx;
			imageOffset += dx;
			rasterOffset += dx;
		}
		if(x + width > Rasterizer2D.bottomX)
		{
			int dx = (x + width) - Rasterizer2D.bottomX;
			width -= dx;
			imageOffset += dx;
			rasterOffset += dx;
		}
		if(!(width <= 0 || height <= 0))
		{
			method349(Rasterizer2D.pixels, myPixels, imageClip, rasterClip, width, height, rasterOffset, imageOffset);
		}
	}

	public void drawSprite(int i, int k, int color) {
		int tempWidth = myWidth + 2;
		int tempHeight = myHeight + 2;
		int[] tempArray = new int[tempWidth * tempHeight];
		for (int x = 0; x < myWidth; x++) {
			for (int y = 0; y < myHeight; y++) {
				if (myPixels[x + y * myWidth] != 0)
					tempArray[(x + 1) + (y + 1) * tempWidth] = myPixels[x + y * myWidth];
			}
		}
		for (int x = 0; x < tempWidth; x++) {
			for (int y = 0; y < tempHeight; y++) {
				if (tempArray[(x) + (y) * tempWidth] == 0) {
					if (x < tempWidth - 1 && tempArray[(x + 1) + ((y) * tempWidth)] > 0 && tempArray[(x + 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (x > 0 && tempArray[(x - 1) + ((y) * tempWidth)] > 0 && tempArray[(x - 1) + ((y) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (y < tempHeight - 1 && tempArray[(x) + ((y + 1) * tempWidth)] > 0 && tempArray[(x) + ((y + 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
					if (y > 0 && tempArray[(x) + ((y - 1) * tempWidth)] > 0 && tempArray[(x) + ((y - 1) * tempWidth)] != 0xffffff) {
						tempArray[(x) + (y) * tempWidth] = color;
					}
				}
			}
		}
		i--;
		k--;
		i += drawOffsetX;
		k += drawOffsetY;
		int l = i + k * Rasterizer2D.width;
		int i1 = 0;
		int j1 = tempHeight;
		int k1 = tempWidth;
		int l1 = Rasterizer2D.width - k1;
		int i2 = 0;
		if (k < Rasterizer2D.topY) {
			int j2 = Rasterizer2D.topY - k;
			j1 -= j2;
			k = Rasterizer2D.topY;
			i1 += j2 * k1;
			l += j2 * Rasterizer2D.width;
		}
		if (k + j1 > Rasterizer2D.bottomY) {
			j1 -= (k + j1) - Rasterizer2D.bottomY;
		}
		if (i < Rasterizer2D.leftX) {
			int k2 = Rasterizer2D.leftX - i;
			k1 -= k2;
			i = Rasterizer2D.leftX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > Rasterizer2D.bottomX) {
			int l2 = (i + k1) - Rasterizer2D.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method349(Rasterizer2D.pixels, tempArray, i1, l, k1, j1, l1, i2);
		}
	}

	public void drawSprite2(int i, int j) {
		int k = 225;// was parameter
		i += drawOffsetX;
		j += drawOffsetY;
		int i1 = i + j * Rasterizer2D.width;
		int j1 = 0;
		int k1 = myHeight;
		int l1 = myWidth;
		int i2 = Rasterizer2D.width - l1;
		int j2 = 0;
		if (j < Rasterizer2D.topY) {
			int k2 = Rasterizer2D.topY - j;
			k1 -= k2;
			j = Rasterizer2D.topY;
			j1 += k2 * l1;
			i1 += k2 * Rasterizer2D.width;
		}
		if (j + k1 > Rasterizer2D.bottomY)
			k1 -= (j + k1) - Rasterizer2D.bottomY;
		if (i < Rasterizer2D.leftX) {
			int l2 = Rasterizer2D.leftX - i;
			l1 -= l2;
			i = Rasterizer2D.leftX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (i + l1 > Rasterizer2D.bottomX) {
			int i3 = (i + l1) - Rasterizer2D.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(l1 <= 0 || k1 <= 0)) {
			method351(j1, l1, Rasterizer2D.pixels, myPixels, j2, k1, i2, k, i1);
		}
	}

	private void method349(int ai[], int ai1[], int j, int k, int l, int i1, int j1, int k1) {
		int i;// was parameter
		int l1 = -(l >> 2);
		l = -(l & 3);
		for (int i2 = -i1; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
			}

			for (int k2 = l; k2 < 0; k2++) {
				i = ai1[j++];
				if (i != 0 && i != -1) {
					ai[k++] = i;
				} else {
					k++;
				}
			}
			k += j1;
			j += k1;
		}
	}

	private void method351(int i, int j, int ai[], int ai1[], int l, int i1, int j1, int k1, int l1) {
		int k;// was parameter
		int j2 = 256 - k1;
		for (int k2 = -i1; k2 < 0; k2++) {
			for (int l2 = -j; l2 < 0; l2++) {
				k = ai1[i++];
				if (k != 0) {
					int i3 = ai[l1];
					ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
				} else {
					l1++;
				}
			}

			l1 += j1;
			i += l;
		}
	}

	public void rotate(int i, int j, int ai[], int k, int ai1[], int i1, int j1, int k1, int l1, int i2) {
		try {
			int j2 = -l1 / 2;
			int k2 = -i / 2;
			int l2 = (int) (Math.sin((double) j / 326.11000000000001D) * 65536D);
			int i3 = (int) (Math.cos((double) j / 326.11000000000001D) * 65536D);
			l2 = l2 * k >> 8;
			i3 = i3 * k >> 8;
			int j3 = (i2 << 16) + (k2 * l2 + j2 * i3);
			int k3 = (i1 << 16) + (k2 * i3 - j2 * l2);
			int l3 = k1 + j1 * Rasterizer2D.width;
			for (j1 = 0; j1 < i; j1++) {
				int i4 = ai1[j1];
				int j4 = l3 + i4;
				int k4 = j3 + i3 * i4;
				int l4 = k3 - l2 * i4;
				for (k1 = -ai[j1]; k1 < 0; k1++) {
					int x1 = k4 >> 16;
				int y1 = l4 >> 16;
				int x2 = x1 + 1;
				int y2 = y1 + 1;
				int c1 = myPixels[x1 + y1 * myWidth];
				int c2 = myPixels[x2 + y1 * myWidth];
				int c3 = myPixels[x1 + y2 * myWidth];
				int c4 = myPixels[x2 + y2 * myWidth];
				int u1 = (k4 >> 8) - (x1 << 8);
				int v1 = (l4 >> 8) - (y1 << 8);
				int u2 = (x2 << 8) - (k4 >> 8);
				int v2 = (y2 << 8) - (l4 >> 8);
				int a1 = u2 * v2;
				int a2 = u1 * v2;
				int a3 = u2 * v1;
				int a4 = u1 * v1;
				int r = (c1 >> 16 & 0xff) * a1 + (c2 >> 16 & 0xff) * a2 + (c3 >> 16 & 0xff) * a3 + (c4 >> 16 & 0xff) * a4 & 0xff0000;
				int g = (c1 >> 8 & 0xff) * a1 + (c2 >> 8 & 0xff) * a2 + (c3 >> 8 & 0xff) * a3 + (c4 >> 8 & 0xff) * a4 >> 8 & 0xff00;
				int b = (c1 & 0xff) * a1 + (c2 & 0xff) * a2 + (c3 & 0xff) * a3 + (c4 & 0xff) * a4 >> 16;
				Rasterizer2D.pixels[j4++] = r | g | b;
					k4 += i3;
					l4 -= l2;
				}

				j3 += l2;
				k3 += i3;
				l3 += Rasterizer2D.width;
			}

		} catch (Exception _ex) {
		}
	}

	public void method353(int i, double d, int l1) {
		// all of the following were parameters
		int j = 15;
		int k = 20;
		int l = 15;
		int j1 = 256;
		int k1 = 20;
		// all of the previous were parameters
		try {
			int i2 = -k / 2;
			int j2 = -k1 / 2;
			int k2 = (int) (Math.sin(d) * 65536D);
			int l2 = (int) (Math.cos(d) * 65536D);
			k2 = k2 * j1 >> 8;
			l2 = l2 * j1 >> 8;
			int i3 = (l << 16) + (j2 * k2 + i2 * l2);
			int j3 = (j << 16) + (j2 * l2 - i2 * k2);
			int k3 = l1 + i * Rasterizer2D.width;
			for (i = 0; i < k1; i++) {
				int l3 = k3;
				int i4 = i3;
				int j4 = j3;
				for (l1 = -k; l1 < 0; l1++) {
					int k4 = myPixels[(i4 >> 16) + (j4 >> 16) * myWidth];
					if (k4 != 0)
						Rasterizer2D.pixels[l3++] = k4;
					else
						l3++;
					i4 += l2;
					j4 -= k2;
				}

				i3 += k2;
				j3 += l2;
				k3 += Rasterizer2D.width;
			}

		} catch (Exception _ex) {
		}
	}

	public Sprite(byte spriteData[]) {
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(spriteData);
			ImageIcon sprite = new ImageIcon(image);
			myWidth = sprite.getIconWidth();
			myHeight = sprite.getIconHeight();
			maxWidth = myWidth;
			maxHeight = myHeight;
			drawOffsetX = 0;
			drawOffsetY = 0;
			myPixels = new int[myWidth * myHeight];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, myWidth, myHeight, myPixels, 0, myWidth);
			pixelgrabber.grabPixels();
			image = null;
			setTransparency(255, 0, 255);
		} catch (Exception _ex) {
			System.out.println(_ex);
		}
	}

	public void method354(IndexedImage background, int i, int j) {
		j += drawOffsetX;
		i += drawOffsetY;
		int k = j + i * Rasterizer2D.width;
		int l = 0;
		int i1 = myHeight;
		int j1 = myWidth;
		int k1 = Rasterizer2D.width - j1;
		int l1 = 0;
		if (i < Rasterizer2D.topY) {
			int i2 = Rasterizer2D.topY - i;
			i1 -= i2;
			i = Rasterizer2D.topY;
			l += i2 * j1;
			k += i2 * Rasterizer2D.width;
		}
		if (i + i1 > Rasterizer2D.bottomY)
			i1 -= (i + i1) - Rasterizer2D.bottomY;
		if (j < Rasterizer2D.leftX) {
			int j2 = Rasterizer2D.leftX - j;
			j1 -= j2;
			j = Rasterizer2D.leftX;
			l += j2;
			k += j2;
			l1 += j2;
			k1 += j2;
		}
		if (j + j1 > Rasterizer2D.bottomX) {
			int k2 = (j + j1) - Rasterizer2D.bottomX;
			j1 -= k2;
			l1 += k2;
			k1 += k2;
		}
		if (!(j1 <= 0 || i1 <= 0)) {
			method355(myPixels, j1, background.palettePixels, i1, Rasterizer2D.pixels, 0, k1, k, l1, l);
		}
	}
	
	public void drawARGBSprite(int xPos, int yPos) {
		drawARGBSprite(xPos, yPos, 256);
	}

	public void drawARGBSprite(int xPos, int yPos, int alpha) {
		int alphaValue = alpha;
		xPos += drawOffsetX;
		yPos += drawOffsetY;
		int i1 = xPos + yPos * Rasterizer2D.width;
		int j1 = 0;
		int spriteHeight = myHeight;
		int spriteWidth = myWidth;
		int i2 = Rasterizer2D.width - spriteWidth;
		int j2 = 0;
		if (yPos < Rasterizer2D.topY) {
			int k2 = Rasterizer2D.topY - yPos;
			spriteHeight -= k2;
			yPos = Rasterizer2D.topY;
			j1 += k2 * spriteWidth;
			i1 += k2 * Rasterizer2D.width;
		}
		if (yPos + spriteHeight > Rasterizer2D.bottomY)
			spriteHeight -= (yPos + spriteHeight) - Rasterizer2D.bottomY;
			if (xPos < Rasterizer2D.leftX) {
			int l2 = Rasterizer2D.leftX - xPos;
			spriteWidth -= l2;
			xPos = Rasterizer2D.leftX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (xPos + spriteWidth > Rasterizer2D.bottomX) {
			int i3 = (xPos + spriteWidth) - Rasterizer2D.bottomX;
			spriteWidth -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(spriteWidth <= 0 || spriteHeight <= 0)) {
			renderARGBPixels(spriteWidth, spriteHeight, myPixels, Rasterizer2D.pixels, i1, alphaValue, j1, j2, i2);
		}
	}

    private void renderARGBPixels(int spriteWidth, int spriteHeight, int spritePixels[], int renderAreaPixels[], int pixel, int alphaValue, int i, int l, int j1) {
    	int pixelColor;
    	int alphaLevel;
    	int alpha = alphaValue;
    	for (int height = -spriteHeight; height < 0; height++) {
    		for (int width = -spriteWidth; width < 0; width++) {
    			alphaValue = ((myPixels[i] >> 24) & (alpha - 1));
    			alphaLevel = 256 - alphaValue;
    			if (alphaLevel > 256) {
    				alphaValue = 0;
    			}
    			if (alpha == 0) {
    				alphaLevel = 256;
    				alphaValue = 0;
    			} 
    			pixelColor = spritePixels[i++];
    			if (pixelColor != 0) {
    				int pixelValue = renderAreaPixels[pixel];
    				renderAreaPixels[pixel++] = ((pixelColor & 0xff00ff) * alphaValue + (pixelValue & 0xff00ff) * alphaLevel & 0xff00ff00) + ((pixelColor & 0xff00) * alphaValue + (pixelValue & 0xff00) * alphaLevel & 0xff0000) >> 8;
    			} else {
    				pixel++;
    			}
    		}
    		pixel += j1;
    		i += l;
    	}
    }

	private void method355(int ai[], int i, byte abyte0[], int j, int ai1[], int k, int l, int i1, int j1, int k1) {
		int l1 = -(i >> 2);
		i = -(i & 3);
		for (int j2 = -j; j2 < 0; j2++) {
			for (int k2 = l1; k2 < 0; k2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			for (int l2 = i; l2 < 0; l2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			i1 += l;
			k1 += j1;
		}

	}

	public int myPixels[];
	public int myWidth;
	public int myHeight;
	int drawOffsetX;
	public int drawOffsetY;
	public int maxWidth;
	public int maxHeight;
}
