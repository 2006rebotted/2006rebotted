package com.runescape.draw;
import com.runescape.collection.Cacheable;

public class Rasterizer2D extends Cacheable {
    /**
     * Sets the Rasterizer2D in the upper left corner with height, width and pixels set.
     * @param height The height of the drawingArea.
     * @param width The width of the drawingArea.
     * @param pixels The array of pixels (RGBColours) in the drawingArea.
     * @param depth An array of fog depths.
     */
	public static void initDrawingArea(int height, int width, int pixels[], float depth[]) {
		depthBuffer = depth;
		Rasterizer2D.pixels = pixels;
		Rasterizer2D.width = width;
		Rasterizer2D.height = height;
		setDrawingArea(height, 0, width, 0);
	}

    /**
     * Draws a transparent box with a gradient that changes from top to bottom.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The width of the box.
     * @param height The height of the box.
     * @param topColour The top rgbColour of the gradient.
     * @param bottomColour The bottom rgbColour of the gradient.
     * @param opacity The opacity value ranging from 0 to 256.
     */
	public static void drawTransparentGradientBox(int leftX, int topY, int width, int height, int topColour, int bottomColour, int opacity) {
		int gradientProgress = 0;
		int progressPerPixel = 0x10000 / height;
		if(leftX < Rasterizer2D.leftX) {
			width -= Rasterizer2D.leftX - leftX;
			leftX = Rasterizer2D.leftX;
		}
		if(topY < Rasterizer2D.topY) {
			gradientProgress += (Rasterizer2D.topY - topY) * progressPerPixel;
			height -= Rasterizer2D.topY - topY;
			topY = Rasterizer2D.topY;
		}
		if(leftX + width > bottomX)
			width = bottomX - leftX;
		if(topY + height > bottomY)
			height = bottomY - topY;
		int leftOver = Rasterizer2D.width - width;
		int transparency = 256 - opacity;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++) {
			int gradient = 0x10000 - gradientProgress >> 8;
			int inverseGradient = gradientProgress >> 8;
			int gradientColour = ((topColour & 0xff00ff) * gradient + (bottomColour & 0xff00ff) * inverseGradient & 0xff00ff00) + ((topColour & 0xff00) * gradient + (bottomColour & 0xff00) * inverseGradient & 0xff0000) >>> 8;
			int transparentPixel = ((gradientColour & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((gradientColour & 0xff00) * opacity >> 8 & 0xff00);
			for(int columnIndex = 0; columnIndex < width; columnIndex++) {
				int backgroundPixel = pixels[pixelIndex];
				backgroundPixel = ((backgroundPixel & 0xff00ff) * transparency >> 8 & 0xff00ff) + ((backgroundPixel & 0xff00) * transparency >> 8 & 0xff00);
				pixels[pixelIndex++] = transparentPixel + backgroundPixel;
			}
			pixelIndex += leftOver;
			gradientProgress += progressPerPixel;
		}
	}

    /**
     * Sets the drawingArea to the default size and position.
     * Position: Upper left corner.
     * Size: As specified before.
     */
	public static void defaultDrawingAreaSize() {
		leftX = 0;
		topY = 0;
		bottomX = width;
		bottomY = height;
		lastX = bottomX;
		viewportCenterX = bottomX / 2;
	}

    /**
     * Sets the drawingArea based on the coordinates of the edges.
     * @param bottomY The bottom edge Y-Coordinate.
     * @param leftX The left edge X-Coordinate.
     * @param rightX The right edge X-Coordinate.
     * @param topY The top edge Y-Coordinate.
     */
	public static void setDrawingArea(int bottomY, int leftX, int rightX, int topY) {
		if(leftX < 0) {
            leftX = 0;
		}
		if(topY < 0) {
            topY = 0;
		}
		if(rightX > width) {
			rightX = width;
		}
		if(bottomY > height) {
			bottomY = height;
		}
		Rasterizer2D.leftX = leftX;
		Rasterizer2D.topY = topY;
		bottomX = rightX;
		Rasterizer2D.bottomY = bottomY;
        lastX = bottomX;
        viewportCenterX = bottomX / 2;
        viewportCenterY = Rasterizer2D.bottomY / 2;
	}

    /**
     * Clears the drawingArea by setting every pixel to 0 (black).
     */
	public static void clear()	{
		int i = width * height;
		for(int j = 0; j < i; j++) {
			pixels[j] = 0;
			depthBuffer[j] = Float.MAX_VALUE;
		}

	}

    /**
     * Draws a box filled with a certain colour.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The width of the box.
     * @param height The height of the box.
     * @param rgbColour The RGBColour of the box.
     */
	public static void drawBox(int leftX, int topY, int width, int height, int rgbColour) {
		if (leftX < Rasterizer2D.leftX) {
            width -= Rasterizer2D.leftX - leftX;
			leftX = Rasterizer2D.leftX;
		}
		if (topY < Rasterizer2D.topY) {
            height -= Rasterizer2D.topY - topY;
			topY = Rasterizer2D.topY;
		}
		if (leftX + width > bottomX)
			width = bottomX - leftX;
		if (topY + height > bottomY)
			height = bottomY - topY;
		int leftOver = Rasterizer2D.width - width;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for (int rowIndex = 0; rowIndex < height; rowIndex++) {
			for (int columnIndex = 0; columnIndex < width; columnIndex++)
				pixels[pixelIndex++] = rgbColour;
			pixelIndex += leftOver;
		}
	}

    /**
     * Draws a transparent box.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The box width.
     * @param height The box height.
     * @param rgbColour The box colour.
     * @param opacity The opacity value ranging from 0 to 256.
     */
	public static void drawTransparentBox(int leftX, int topY, int width, int height, int rgbColour, int opacity){
		if(leftX < Rasterizer2D.leftX){
            width -= Rasterizer2D.leftX - leftX;
			leftX = Rasterizer2D.leftX;
		}
		if(topY < Rasterizer2D.topY){
            height -= Rasterizer2D.topY - topY;
            topY = Rasterizer2D.topY;
		}
		if(leftX + width > bottomX)
			width = bottomX - leftX;
		if(topY + height > bottomY)
			height = bottomY - topY;
		int transparency = 256 - opacity;
		int red = (rgbColour >> 16 & 0xff) * opacity;
		int green = (rgbColour >> 8 & 0xff) * opacity;
		int blue = (rgbColour & 0xff) * opacity;
		int leftOver = Rasterizer2D.width - width;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++){
			for(int columnIndex = 0; columnIndex < width; columnIndex++){
				int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
				int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
				int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
				int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
				pixels[pixelIndex++] = transparentColour;
			}
			pixelIndex += leftOver;
		}
	}
	
    /**
     * Draws a 1 pixel thick box outline in a certain colour.
     * @param leftX The left edge X-Coordinate.
     * @param topY The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     */
	public static void drawBoxOutline(int leftX, int topY, int width, int height, int rgbColour){
		drawHorizontalLine(leftX, topY, width, rgbColour);
		drawHorizontalLine(leftX, (topY + height) - 1, width, rgbColour);
		drawVerticalLine(leftX, topY, height, rgbColour);
		drawVerticalLine((leftX + width) - 1, topY, height, rgbColour);
	}

    /**
     * Draws a coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     */
	public static void drawHorizontalLine(int xPosition, int yPosition, int width, int rgbColour){
		if(yPosition < topY || yPosition >= bottomY)
			return;
		if(xPosition < leftX){
			width -= leftX - xPosition;
			xPosition = leftX;
		}
		if(xPosition + width > bottomX)
            width = bottomX - xPosition;
		int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
		for(int i = 0; i < width; i++)
			pixels[pixelIndex + i] = rgbColour;
	}

    /**
     * Draws a coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     */
	public static void drawVerticalLine(int xPosition, int yPosition, int height, int rgbColour){
		if(xPosition < leftX || xPosition >= bottomX)
			return;
		if(yPosition < topY){
			height -= topY - yPosition;
			yPosition = topY;
		}
		if(yPosition + height > bottomY)
			height = bottomY - yPosition;
		int pixelIndex = xPosition + yPosition * width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++)
			pixels[pixelIndex + rowIndex * width] = rgbColour;
	}

    /**
     * Draws a 1 pixel thick transparent box outline in a certain colour.
     * @param leftX The left edge X-Coordinate
     * @param topY The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     * @param opacity The opacity value ranging from 0 to 256.
     */
	public static void drawTransparentBoxOutline(int leftX, int topY, int width, int height, int rgbColour, int opacity) {
		drawTransparentHorizontalLine(leftX, topY, width, rgbColour, opacity);
		drawTransparentHorizontalLine(leftX, topY + height - 1, width, rgbColour, opacity);
		if(height >= 3) {
			drawTransparentVerticalLine(leftX, topY + 1, height - 2, rgbColour, opacity);
			drawTransparentVerticalLine(leftX + width - 1, topY + 1, height - 2, rgbColour, opacity);
		}
	}

    /**
     * Draws a transparent coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     * @param opacity The opacity value ranging from 0 to 256.
     */
	public static void drawTransparentHorizontalLine(int xPosition, int yPosition, int width, int rgbColour, int opacity) {
		if(yPosition < topY || yPosition >= bottomY) {
			return;
		}
		if(xPosition < leftX) {
            width -= leftX - xPosition;
			xPosition = leftX;
		}
		if(xPosition + width > bottomX) {
			width = bottomX - xPosition;
		}
		final int transparency = 256 - opacity;
		final int red = (rgbColour >> 16 & 0xff) * opacity;
		final int green = (rgbColour >> 8 & 0xff) * opacity;
		final int blue = (rgbColour & 0xff) * opacity;
		int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
		for(int i = 0; i < width; i++) {
			final int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
			final int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
			final int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
			final int transparentColour = (red + otherRed >> 8 << 16) + (green + otherGreen >> 8 << 8) + (blue + otherBlue >> 8);
			pixels[pixelIndex++] = transparentColour;
		}
	}

    /**
     * Draws a transparent coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     * @param opacity The opacity value ranging from 0 to 256.
     */
	public static void drawTransparentVerticalLine(int xPosition, int yPosition, int height, int rgbColour, int opacity) {
		if(xPosition < leftX || xPosition >= bottomX) {
			return;
		}
		if(yPosition < topY) {
			height -= topY - yPosition;
			yPosition = topY;
		}
		if(yPosition + height > bottomY) {
			height = bottomY - yPosition;
		}
		final int transparency = 256 - opacity;
		final int red = (rgbColour >> 16 & 0xff) * opacity;
		final int green = (rgbColour >> 8 & 0xff) * opacity;
		final int blue = (rgbColour & 0xff) * opacity;
		int pixelIndex = xPosition + yPosition * width;
		for(int i = 0; i < height; i++) {
			final int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
			final int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
			final int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
			final int transparentColour = (red + otherRed >> 8 << 16) + (green + otherGreen >> 8 << 8) + (blue + otherBlue >> 8);
			pixels[pixelIndex] = transparentColour;
			pixelIndex += width;
		}
	}
	public static float depthBuffer[];
    public static int pixels[];
    public static int width;
    public static int height;
    public static int topY;
    public static int bottomY;
    public static int leftX;
    public static int bottomX;
    public static int lastX;
    public static int viewportCenterX;
    public static int viewportCenterY;
}