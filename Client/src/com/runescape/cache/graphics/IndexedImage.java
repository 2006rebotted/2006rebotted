package com.runescape.cache.graphics;
import com.runescape.cache.FileArchive;
import com.runescape.draw.Rasterizer2D;
import com.runescape.io.Buffer;

public final class IndexedImage extends Rasterizer2D {
	
	public byte palettePixels[];
	public final int[] palette;
	public int width;
	public int height;
	public int drawOffsetX;
	public int drawOffsetY;
	public int resizeWidth;	
	private int resizeHeight;

	public IndexedImage(FileArchive archive, String s, int i) {		
		Buffer image = new Buffer(archive.readFile(s + ".dat"));
		Buffer meta = new Buffer(archive.readFile("index.dat"));		
		meta.currentPosition = image.readUShort();
		resizeWidth = meta.readUShort();
		resizeHeight = meta.readUShort();
		
		int colorLength = meta.readUnsignedByte();		
		palette = new int[colorLength];
		
		for(int index = 0; index < colorLength - 1; index++) {
			palette[index + 1] = meta.readTriByte();			
		}
		
		for(int l = 0; l < i; l++) {			
			meta.currentPosition += 2;
			image.currentPosition += meta.readUShort() * meta.readUShort();
			meta.currentPosition++;
		}
		drawOffsetX = meta.readUnsignedByte();		
		drawOffsetY = meta.readUnsignedByte();		
		width = meta.readUShort();
		height = meta.readUShort();		
		int type = meta.readUnsignedByte();		
		int pixels = width * height;			
		palettePixels = new byte[pixels];
		
		if(type == 0) {
			for(int index = 0; index < pixels; index++) {
				palettePixels[index] = image.readSignedByte();
			}
		} else if(type == 1) {
			for(int x = 0; x < width; x++) {				
				for(int y = 0; y < height; y++) {					
					palettePixels[x + y * width] = image.readSignedByte();
				}
			}
		}
	}

	public void downscale() {
		resizeWidth /= 2;
		resizeHeight /= 2;
		byte raster[] = new byte[resizeWidth * resizeHeight];		
		int sourceIndex = 0;		
		for(int y  = 0; y  < height; y ++) {			
			for(int x = 0; x < width; x++) {				
				raster[(x + drawOffsetX >> 1) + (y  + drawOffsetY >> 1) * resizeWidth] = raster[sourceIndex++];
			}
		}
		this.palettePixels = raster;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void resize() {
		if(width == resizeWidth && height == resizeHeight) {
			return;
		}
		
		byte raster[] = new byte[resizeWidth * resizeHeight];
		
		int i = 0;
		for(int y = 0; y < height; y++) {			
			for(int x = 0; x < width; x++) {				
				raster[x + drawOffsetX + (y + drawOffsetY) * resizeWidth] = raster[i++];
			}
		}
		this.palettePixels = raster;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void flipHorizontally() {
		byte raster[] = new byte[width * height];		
		int pixel = 0;		
		for(int y = 0; y < height; y++) {			
			for(int x = width - 1; x >= 0; x--) {				
				raster[pixel++] = raster[x + y * width];
			}
		}
		this.palettePixels = raster;
		drawOffsetX = resizeWidth - width - drawOffsetX;
	}

	public void flipVertically() {
		byte raster[] = new byte[width * height];		
		int pixel = 0;		
		for(int y = height - 1; y >= 0; y--) {			
			for(int x = 0; x < width; x++) {				
				raster[pixel++] = raster[x + y * width];
			}
		}
		this.palettePixels = raster;
		drawOffsetY = resizeHeight - height - drawOffsetY;
	}

	public void offsetColor(int redOffset, int greenOffset, int blueOffset) {		
		for(int index = 0; index < palette.length; index++) {			
			int red = palette[index] >> 16 & 0xff;		
			red += redOffset;
			
			if(red < 0) {
				red = 0;
			} else if(red > 255) {
				red = 255;
			}
			
			int green = palette[index] >> 8 & 0xff;
		
			green += greenOffset;			
			if(green < 0) {
				green = 0;
			} else if(green > 255) {
				green = 255;
			}
			
			int blue = palette[index] & 0xff;
			
			blue += blueOffset;			
			if(blue < 0) {
				blue = 0;
			} else if(blue > 255) {
				blue = 255;
			}
			palette[index] = (red << 16) + (green << 8) + blue;
		}
	}

	public void draw(int x, int y) {		
		x += drawOffsetX;
		y += drawOffsetY;		
		int destOffset = x + y * Rasterizer2D.width;		
		int sourceOffset = 0;
		int height = this.height;
		int width = this.width;		
		int destStep = Rasterizer2D.width - width;		
		int sourceStep = 0;
		
		if(y < Rasterizer2D.topY) {
			int dy = Rasterizer2D.topY - y;			
			height -= dy;			
			y = Rasterizer2D.topY;
			sourceOffset += dy * width;			
			destOffset += dy * Rasterizer2D.width;
		}
		
		if(y + height > Rasterizer2D.bottomY) {
			height -= (y + height) - Rasterizer2D.bottomY;
		}
		
		if(x < Rasterizer2D.leftX) {
			int k2 = Rasterizer2D.leftX - x;
			width -= k2;
			x = Rasterizer2D.leftX;
			sourceOffset += k2;
			destOffset += k2;
			sourceStep += k2;
			destStep += k2;
		}
		
		if(x + width > Rasterizer2D.bottomX) {
			int dx = (x + width) - Rasterizer2D.bottomX;			
			width -= dx;
			sourceStep += dx;
			destStep += dx;
		}
		
		if(!(width <= 0 || height <= 0)) {			
			draw(height, Rasterizer2D.pixels, palettePixels, destStep, destOffset, width, sourceOffset, palette, sourceStep);
		}
		
	}

	private void draw(int i, int raster[], byte image[], int destStep, int destIndex, int width,  int sourceIndex, int ai1[], int sourceStep) {
		int minX = -(width >> 2);		
		width = -(width & 3);		
		for(int y = -i; y < 0; y++) {			
			for(int x = minX; x < 0; x++) {	
				
				byte pixel = image[sourceIndex++];
				
				if(pixel != 0) {
					raster[destIndex++] = ai1[pixel & 0xff];					
				} else {
					destIndex++;					
				}
				pixel = image[sourceIndex++];
				if(pixel != 0) {
					raster[destIndex++] = ai1[pixel & 0xff];
				} else {
					destIndex++;
				}
				pixel = image[sourceIndex++];				
				if(pixel != 0) {
					raster[destIndex++] = ai1[pixel & 0xff];
				} else {
					destIndex++;
				}
				pixel = image[sourceIndex++];
				if(pixel != 0) {
					raster[destIndex++] = ai1[pixel & 0xff];
				} else {
					destIndex++;
				}
			}
			for(int x = width; x < 0; x++) {				
				byte pixel = image[sourceIndex++];				
				if(pixel != 0) {
					raster[destIndex++] = ai1[pixel & 0xff];
				} else {
					destIndex++;
				}
			}
			destIndex += destStep;			
			sourceIndex += sourceStep;			
		}
	}
}