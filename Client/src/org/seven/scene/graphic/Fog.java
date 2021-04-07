package org.seven.scene.graphic;
import com.runescape.draw.Rasterizer2D;
import com.runescape.draw.Rasterizer3D;

public class Fog {    
    /**
     * Grabs distance from player.
     */
    private float fogDistance;
    
    /**
     * Sets the fog color.
     */
    public static int setColor = 0xA7C5C7;
    
    /**
     * 
     * @param fogStartDistance
     * @param fogEndDistance
     * @param fogIntensity
     */
    public void renderFog(boolean belowGround, int fogStartDistance, int fogEndDistance, int fogIntensity) {
        getColor(setColor);
        int pos = Rasterizer3D.scanOffsets[0];
        int src, dst, alpha;
        int fogBegin = (int) (fogStartDistance + fogDistance);
        int fogEnd = (int) (fogEndDistance + fogDistance);
        for (int y = 0; y < Rasterizer2D.bottomY; y++) {
            for (int x = 0; x < Rasterizer2D.lastX; x++) {
                if (Rasterizer2D.depthBuffer[pos] >= fogEnd) {
                    Rasterizer2D.pixels[pos] = setColor;
                } else if (Rasterizer2D.depthBuffer[pos] >= fogBegin) {
                    alpha = (int)(Rasterizer2D.depthBuffer[pos] - fogBegin) / fogIntensity;
                    src = ((setColor & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((setColor & 0xff00) * alpha >> 8 & 0xff00);
                    alpha = 256 - alpha;
                    dst = Rasterizer2D.pixels[pos];
                    dst = ((dst & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((dst & 0xff00) * alpha >> 8 & 0xff00);
                    Rasterizer2D.pixels[pos] = src + dst;
                }
                pos++;
            }
            pos += Rasterizer2D.width - Rasterizer2D.lastX;
        }
    }
    
    /**
     * 
     * @param fogDistance
     */
    public void setFogDistance(float fogDistance) {
        this.fogDistance = fogDistance;
    }
    
    /**
     * 
     * @param fogColor
     */
    public void getColor(int fogColor) {
        setColor = fogColor;
    }
}
