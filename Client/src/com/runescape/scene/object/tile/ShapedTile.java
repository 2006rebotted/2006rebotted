package com.runescape.scene.object.tile;

public final class ShapedTile{
	public ShapedTile(int yLoc, int j, int k, int l, int texture, int j1, int rotation, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int xLoc, int l4){
        flat = !(i3 != l2 || i3 != l || i3 != k2);
		shape = j3;
		this.rotation = rotation;
		colourRGB = i2;
		colourRGBA = l4;
		char sideLength = 128;
		int halfSizeLength = sideLength / 2;
		int quarterSizeLight = sideLength / 4;
		int k5 = (sideLength * 3) / 4;
		int ai[] = anIntArrayArray696[j3];
		int l5 = ai.length;
		anIntArray673 = new int[l5];
		anIntArray674 = new int[l5];
		anIntArray675 = new int[l5];
		int ai1[] = new int[l5];
		int ai2[] = new int[l5];
		int xPos = xLoc * sideLength;
		int yPos = yLoc * sideLength;
		for(int k6 = 0; k6 < l5; k6++){
			int realShape = ai[k6];
			if((realShape & 1) == 0 && realShape <= 8)
				realShape = (realShape - rotation - rotation - 1 & 7) + 1;
			if(realShape > 8 && realShape <= 12)
				realShape = (realShape - 9 - rotation & 3) + 9;
			if(realShape > 12 && realShape <= 16)
				realShape = (realShape - 13 - rotation & 3) + 13;
			int i7;
			int k7;
			int i8;
			int k8;
			int j9;
			if(realShape == 1){
				i7 = xPos;
				k7 = yPos;
				i8 = i3;
				k8 = l1;
				j9 = j;
			} else if(realShape == 2){
				i7 = xPos + halfSizeLength;
				k7 = yPos;
				i8 = i3 + l2 >> 1;
				k8 = l1 + i4 >> 1;
				j9 = j + l3 >> 1;
			} else if(realShape == 3){
				i7 = xPos + sideLength;
				k7 = yPos;
				i8 = l2;
				k8 = i4;
				j9 = l3;
			} else if(realShape == 4){
				i7 = xPos + sideLength;
				k7 = yPos + halfSizeLength;
				i8 = l2 + l >> 1;
				k8 = i4 + j2 >> 1;
				j9 = l3 + j1 >> 1;
			} else if(realShape == 5){
				i7 = xPos + sideLength;
				k7 = yPos + sideLength;
				i8 = l;
				k8 = j2;
				j9 = j1;
			} else if(realShape == 6){
				i7 = xPos + halfSizeLength;
				k7 = yPos + sideLength;
				i8 = l + k2 >> 1;
				k8 = j2 + k >> 1;
				j9 = j1 + k3 >> 1;
			} else if(realShape == 7){
				i7 = xPos;
				k7 = yPos + sideLength;
				i8 = k2;
				k8 = k;
				j9 = k3;
			} else if(realShape == 8){
				i7 = xPos;
				k7 = yPos + halfSizeLength;
				i8 = k2 + i3 >> 1;
				k8 = k + l1 >> 1;
				j9 = k3 + j >> 1;
			} else if(realShape == 9){
				i7 = xPos + halfSizeLength;
				k7 = yPos + quarterSizeLight;
				i8 = i3 + l2 >> 1;
				k8 = l1 + i4 >> 1;
				j9 = j + l3 >> 1;
			} else if(realShape == 10){
				i7 = xPos + k5;
				k7 = yPos + halfSizeLength;
				i8 = l2 + l >> 1;
				k8 = i4 + j2 >> 1;
				j9 = l3 + j1 >> 1;
			} else if(realShape == 11){
				i7 = xPos + halfSizeLength;
				k7 = yPos + k5;
				i8 = l + k2 >> 1;
				k8 = j2 + k >> 1;
				j9 = j1 + k3 >> 1;
			} else if(realShape == 12){
				i7 = xPos + quarterSizeLight;
				k7 = yPos + halfSizeLength;
				i8 = k2 + i3 >> 1;
				k8 = k + l1 >> 1;
				j9 = k3 + j >> 1;
			} else if(realShape == 13){
				i7 = xPos + quarterSizeLight;
				k7 = yPos + quarterSizeLight;
				i8 = i3;
				k8 = l1;
				j9 = j;
			} else if(realShape == 14){
				i7 = xPos + k5;
				k7 = yPos + quarterSizeLight;
				i8 = l2;
				k8 = i4;
				j9 = l3;
			} else if(realShape == 15){
				i7 = xPos + k5;
				k7 = yPos + k5;
				i8 = l;
				k8 = j2;
				j9 = j1;
			} else {
				i7 = xPos + quarterSizeLight;
				k7 = yPos + k5;
				i8 = k2;
				k8 = k;
				j9 = k3;
			}
			anIntArray673[k6] = i7;
			anIntArray674[k6] = i8;
			anIntArray675[k6] = k7;
			ai1[k6] = k8;
			ai2[k6] = j9;
		}

		int ai3[] = anIntArrayArray697[j3];
		int j7 = ai3.length / 4;
		anIntArray679 = new int[j7];
		anIntArray680 = new int[j7];
		anIntArray681 = new int[j7];
		anIntArray676 = new int[j7];
		anIntArray677 = new int[j7];
		anIntArray678 = new int[j7];
		if(texture != -1)
			anIntArray682 = new int[j7];
		int l7 = 0;
		for(int j8 = 0; j8 < j7; j8++){
			int l8 = ai3[l7];
			int k9 = ai3[l7 + 1];
			int i10 = ai3[l7 + 2];
			int k10 = ai3[l7 + 3];
			l7 += 4;
			if(k9 < 4)
				k9 = k9 - rotation & 3;
			if(i10 < 4)
				i10 = i10 - rotation & 3;
			if(k10 < 4)
				k10 = k10 - rotation & 3;
			anIntArray679[j8] = k9;
			anIntArray680[j8] = i10;
			anIntArray681[j8] = k10;
			if(l8 == 0){
				anIntArray676[j8] = ai1[k9];
				anIntArray677[j8] = ai1[i10];
				anIntArray678[j8] = ai1[k10];
				if(anIntArray682 != null)
					anIntArray682[j8] = -1;
			} else {
				anIntArray676[j8] = ai2[k9];
				anIntArray677[j8] = ai2[i10];
				anIntArray678[j8] = ai2[k10];
				if(anIntArray682 != null)
					anIntArray682[j8] = texture;
			}
		}

		int i9 = i3;
		int l9 = l2;
		if(l2 < i9)
			i9 = l2;
		if(l2 > l9)
			l9 = l2;
		if(l < i9)
			i9 = l;
		if(l > l9)
			l9 = l;
		if(k2 < i9)
			i9 = k2;
		if(k2 > l9)
			l9 = k2;
		i9 /= 14;
		l9 /= 14;
	}

	public final int[] anIntArray673;
	public final int[] anIntArray674;
	public final int[] anIntArray675;
	public final int[] anIntArray676;
	public final int[] anIntArray677;
	public final int[] anIntArray678;
	public final int[] anIntArray679;
	public final int[] anIntArray680;
	public final int[] anIntArray681;
	public int anIntArray682[];
	public final boolean flat;
	public final int shape;
	public final int rotation;
	public final int colourRGB;
	public final int colourRGBA;
	public static final int[] anIntArray688 = new int[6];
	public static final int[] anIntArray689 = new int[6];
	public static final int[] anIntArray690 = new int[6];
	public static final int[] anIntArray691 = new int[6];
	public static final int[] anIntArray692 = new int[6];
	public static final int[] depthPoint = new int[6];
	static final int[] anIntArray693 = {
		1, 0
	};
	static final int[] anIntArray694 = {
		2, 1
	};
	static final int[] anIntArray695 = {
		3, 3
	};
	private static final int[][] anIntArrayArray696 = {
		{
			1, 3, 5, 7
		}, {
			1, 3, 5, 7
		}, {
			1, 3, 5, 7
		}, {
			1, 3, 5, 7, 6
		}, {
			1, 3, 5, 7, 6
		}, {
			1, 3, 5, 7, 6
		}, {
			1, 3, 5, 7, 6
		}, {
			1, 3, 5, 7, 2, 6
		}, {
			1, 3, 5, 7, 2, 8
		}, {
			1, 3, 5, 7, 2, 8
		}, {
			1, 3, 5, 7, 11, 12
		}, {
			1, 3, 5, 7, 11, 12
		}, {
			1, 3, 5, 7, 13, 14
		}
	};
	private static final int[][] anIntArrayArray697 = {
		{
			0, 1, 2, 3, 0, 0, 1, 3
		}, {
			1, 1, 2, 3, 1, 0, 1, 3
		}, {
			0, 1, 2, 3, 1, 0, 1, 3
		}, {
			0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 
			4, 3
		}, {
			0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 
			2, 4
		}, {
			0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 
			2, 4
		}, {
			0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 
			4, 3
		}, {
			0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 
			4, 5, 1, 0, 5, 3
		}, {
			0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 
			3, 5, 1, 0, 4, 5
		}, {
			0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 
			2, 3, 1, 4, 3, 5
		}, {
			0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 
			2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 
			1, 4, 2, 3
		}, {
			1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 
			2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 
			0, 4, 2, 3
		}, {
			1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 
			4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 
			0, 1, 2, 5
		}
	};

}
