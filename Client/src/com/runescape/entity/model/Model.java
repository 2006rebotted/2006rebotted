package com.runescape.entity.model;
import com.runescape.Configuration;
import com.runescape.cache.anim.Frame;
import com.runescape.cache.anim.FrameBase;
import com.runescape.draw.Rasterizer2D;
import com.runescape.draw.Rasterizer3D;
import com.runescape.entity.Renderable;
import com.runescape.io.Buffer;
import com.runescape.net.requester.Provider;
import com.runescape.scene.SceneGraph;

public class Model extends Renderable {

	public static void clear() {	      
		aClass21Array1661 = null;
		aBooleanArray1663 = null;
		aBooleanArray1664 = null;
		anIntArray1666 = null;
		anIntArray1667 = null;
		anIntArray1668 = null;
		anIntArray1669 = null;
		anIntArray1670 = null;
		anIntArray1671 = null;
		anIntArrayArray1672 = null;
		anIntArray1673 = null;
		anIntArrayArray1674 = null;
		anIntArray1675 = null;
		anIntArray1676 = null;
		anIntArray1677 = null;
		SINE = null;
		COSINE = null;
		modelIntArray3 = null;
		modelIntArray4 = null;
	}

	public Model(int modelId) {
		int j = -870;
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		ModelHeader modelHeader = aClass21Array1661[modelId];		
		numVertices = modelHeader.anInt369;
		numTriangles = modelHeader.anInt370;
		anInt1642 = modelHeader.anInt371;
		vertexX = new int[numVertices];
		vertexY = new int[numVertices];
		vertexZ = new int[numVertices];
		facePointA = new int[numTriangles];
		facePointB = new int[numTriangles];
		while (j >= 0)
			aBoolean1618 = !aBoolean1618;
		facePointC = new int[numTriangles];
		anIntArray1643 = new int[anInt1642];
		anIntArray1644 = new int[anInt1642];
		anIntArray1645 = new int[anInt1642];
		if (modelHeader.anInt376 >= 0)
			anIntArray1655 = new int[numVertices];
		if (modelHeader.anInt380 >= 0)
			faceDrawType = new int[numTriangles];
		if (modelHeader.anInt381 >= 0)
			anIntArray1638 = new int[numTriangles];
		else
			anInt1641 = -modelHeader.anInt381 - 1;
		if (modelHeader.anInt382 >= 0)
			anIntArray1639 = new int[numTriangles];
		if (modelHeader.anInt383 >= 0)
			anIntArray1656 = new int[numTriangles];
		triangleColours = new int[numTriangles];
		Buffer buffer = new Buffer(modelHeader.aByteArray368);
		buffer.currentPosition = modelHeader.anInt372;
		Buffer stream_1 = new Buffer(modelHeader.aByteArray368);
		stream_1.currentPosition = modelHeader.anInt373;
		Buffer stream_2 = new Buffer(modelHeader.aByteArray368);
		stream_2.currentPosition = modelHeader.anInt374;
		Buffer stream_3 = new Buffer(modelHeader.aByteArray368);
		stream_3.currentPosition = modelHeader.anInt375;
		Buffer stream_4 = new Buffer(modelHeader.aByteArray368);
		stream_4.currentPosition = modelHeader.anInt376;
		int k = 0;
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < numVertices; j1++) {
			int k1 = buffer.readUnsignedByte();
			int i2 = 0;
			if ((k1 & 1) != 0)
				i2 = stream_1.readSmart();
			int k2 = 0;
			if ((k1 & 2) != 0)
				k2 = stream_2.readSmart();
			int i3 = 0;
			if ((k1 & 4) != 0)
				i3 = stream_3.readSmart();
			vertexX[j1] = k + i2;
			vertexY[j1] = l + k2;
			vertexZ[j1] = i1 + i3;
			k = vertexX[j1];
			l = vertexY[j1];
			i1 = vertexZ[j1];
			if (anIntArray1655 != null)
				anIntArray1655[j1] = stream_4.readUnsignedByte();
		}
		buffer.currentPosition = modelHeader.anInt379;
		stream_1.currentPosition = modelHeader.anInt380;
		stream_2.currentPosition = modelHeader.anInt381;
		stream_3.currentPosition = modelHeader.anInt382;
		stream_4.currentPosition = modelHeader.anInt383;
		for (int l1 = 0; l1 < numTriangles; l1++) {
			triangleColours[l1] = buffer.readUShort();			
			if (faceDrawType != null)
				faceDrawType[l1] = stream_1.readUnsignedByte();
			if (anIntArray1638 != null)
				anIntArray1638[l1] = stream_2.readUnsignedByte();
			if (anIntArray1639 != null) {
				anIntArray1639[l1] = stream_3.readUnsignedByte();
			}
			if (anIntArray1656 != null)
				anIntArray1656[l1] = stream_4.readUnsignedByte();
		}
		buffer.currentPosition = modelHeader.anInt377;
		stream_1.currentPosition = modelHeader.anInt378;
		int j2 = 0;
		int l2 = 0;
		int j3 = 0;
		int k3 = 0;
		for (int l3 = 0; l3 < numTriangles; l3++) {
			int i4 = stream_1.readUnsignedByte();
			if (i4 == 1) {
				j2 = buffer.readSmart() + k3;
				k3 = j2;
				l2 = buffer.readSmart() + k3;
				k3 = l2;
				j3 = buffer.readSmart() + k3;
				k3 = j3;
				facePointA[l3] = j2;
				facePointB[l3] = l2;
				facePointC[l3] = j3;
			}
			if (i4 == 2) {
				l2 = j3;
				j3 = buffer.readSmart() + k3;
				k3 = j3;
				facePointA[l3] = j2;
				facePointB[l3] = l2;
				facePointC[l3] = j3;
			}
			if (i4 == 3) {
				j2 = j3;
				j3 = buffer.readSmart() + k3;
				k3 = j3;
				facePointA[l3] = j2;
				facePointB[l3] = l2;
				facePointC[l3] = j3;
			}
			if (i4 == 4) {
				int k4 = j2;
				j2 = l2;
				l2 = k4;
				j3 = buffer.readSmart() + k3;
				k3 = j3;
				facePointA[l3] = j2;
				facePointB[l3] = l2;
				facePointC[l3] = j3;
			}
		}
		buffer.currentPosition = modelHeader.anInt384;
		for (int j4 = 0; j4 < anInt1642; j4++) {
			anIntArray1643[j4] = buffer.readUShort();
			anIntArray1644[j4] = buffer.readUShort();
			anIntArray1645[j4] = buffer.readUShort();
		}
	}

	public void scale2(int i) {
		for (int i1 = 0; i1 < numVertices; i1++) {
			vertexX[i1] = vertexX[i1] / i;
			vertexY[i1] = vertexY[i1] / i;
			vertexZ[i1] = vertexZ[i1] / i;
		}
	}

	public static void method460(byte abyte0[], int j) {
	try {
		if (abyte0 == null) {
			ModelHeader class21 = aClass21Array1661[j] = new ModelHeader();
			class21.anInt369 = 0;
			class21.anInt370 = 0;
			class21.anInt371 = 0;
			return;
		}
		Buffer stream = new Buffer(abyte0);
		stream.currentPosition = abyte0.length - 18;
		ModelHeader class21_1 = aClass21Array1661[j] = new ModelHeader();
		class21_1.aByteArray368 = abyte0;
		class21_1.anInt369 = stream.readUShort();
		class21_1.anInt370 = stream.readUShort();
		class21_1.anInt371 = stream.readUnsignedByte();
		int k = stream.readUnsignedByte();
		int l = stream.readUnsignedByte();
		int i1 = stream.readUnsignedByte();
		int j1 = stream.readUnsignedByte();
		int k1 = stream.readUnsignedByte();
		int l1 = stream.readUShort();
		int i2 = stream.readUShort();
		int j2 = stream.readUShort();
		int k2 = stream.readUShort();
		int l2 = 0;
		class21_1.anInt372 = l2;
		l2 += class21_1.anInt369;
		class21_1.anInt378 = l2;
		l2 += class21_1.anInt370;
		class21_1.anInt381 = l2;
		if (l == 255)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt381 = -l - 1;
		class21_1.anInt383 = l2;
		if (j1 == 1)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt383 = -1;
		class21_1.anInt380 = l2;
		if (k == 1)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt380 = -1;
		class21_1.anInt376 = l2;
		if (k1 == 1)
			l2 += class21_1.anInt369;
		else
			class21_1.anInt376 = -1;
		class21_1.anInt382 = l2;
		if (i1 == 1)
			l2 += class21_1.anInt370;
		else
			class21_1.anInt382 = -1;
		class21_1.anInt377 = l2;
		l2 += k2;
		class21_1.anInt379 = l2;
		l2 += class21_1.anInt370 * 2;
		class21_1.anInt384 = l2;
		l2 += class21_1.anInt371 * 6;
		class21_1.anInt373 = l2;
		l2 += l1;
		class21_1.anInt374 = l2;
		l2 += i2;
		class21_1.anInt375 = l2;
		l2 += j2;
		} catch (Exception _ex) {
		}
	}

	public static boolean newmodel[];

	public static void method459(int i,
			Provider onDemandFetcherParent) {
		aClass21Array1661 = new ModelHeader[80000];
		newmodel = new boolean[100000];
		resourceProvider = onDemandFetcherParent;
	}

	public static void method461(int j) {
		aClass21Array1661[j] = null;
	}

	public static Model getModel(int file) {
		if (aClass21Array1661 == null)
			return null;
		ModelHeader class21 = aClass21Array1661[file];
		if (class21 == null) {
			resourceProvider.provide(file);
			return null;
		} else {
			return new Model(file);
		}
	}

	public static boolean isCached(int file) {
		if (aClass21Array1661 == null)
			return false;

		ModelHeader class21 = aClass21Array1661[file];
		if (class21 == null) {
			resourceProvider.provide(file);
			return false;
		} else {
			return true;
		}
	}

	private Model(boolean flag) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		if (!flag)
			aBoolean1618 = !aBoolean1618;
	}

	public Model(int i, Model amodel[]) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		numVertices = 0;
		numTriangles = 0;
		anInt1642 = 0;
		anInt1641 = -1;
		for (int k = 0; k < i; k++) {
			Model model = amodel[k];
			if (model != null) {
				numVertices += model.numVertices;
				numTriangles += model.numTriangles;
				anInt1642 += model.anInt1642;
				flag |= model.faceDrawType != null;
				if (model.anIntArray1638 != null) {
					flag1 = true;
				} else {
					if (anInt1641 == -1)
						anInt1641 = model.anInt1641;
					if (anInt1641 != model.anInt1641)
						flag1 = true;
				}
				flag2 |= model.anIntArray1639 != null;
				flag3 |= model.anIntArray1656 != null;
			}
		}

		vertexX = new int[numVertices];
		vertexY = new int[numVertices];
		vertexZ = new int[numVertices];
		anIntArray1655 = new int[numVertices];
		facePointA = new int[numTriangles];
		facePointB = new int[numTriangles];
		facePointC = new int[numTriangles];
		anIntArray1643 = new int[anInt1642];
		anIntArray1644 = new int[anInt1642];
		anIntArray1645 = new int[anInt1642];
		if (flag)
			faceDrawType = new int[numTriangles];
		if (flag1)
			anIntArray1638 = new int[numTriangles];
		if (flag2)
			anIntArray1639 = new int[numTriangles];
		if (flag3)
			anIntArray1656 = new int[numTriangles];
		triangleColours = new int[numTriangles];
		numVertices = 0;
		numTriangles = 0;
		anInt1642 = 0;
		int l = 0;
		for (int i1 = 0; i1 < i; i1++) {
			Model model_1 = amodel[i1];
			if (model_1 != null) {
				for (int j1 = 0; j1 < model_1.numTriangles; j1++) {
					if (flag)
						if (model_1.faceDrawType == null) {
							faceDrawType[numTriangles] = 0;
						} else {
							int k1 = model_1.faceDrawType[j1];
							if ((k1 & 2) == 2)
								k1 += l << 2;
							faceDrawType[numTriangles] = k1;
						}
					if (flag1)
						if (model_1.anIntArray1638 == null)
							anIntArray1638[numTriangles] = model_1.anInt1641;
						else
							anIntArray1638[numTriangles] = model_1.anIntArray1638[j1];
					if (flag2)
						if (model_1.anIntArray1639 == null)
							anIntArray1639[numTriangles] = 0;
						else
							anIntArray1639[numTriangles] = model_1.anIntArray1639[j1];

					if (flag3 && model_1.anIntArray1656 != null)
						anIntArray1656[numTriangles] = model_1.anIntArray1656[j1];
					triangleColours[numTriangles] = model_1.triangleColours[j1];
					facePointA[numTriangles] = method465(model_1,
							model_1.facePointA[j1]);
					facePointB[numTriangles] = method465(model_1,
							model_1.facePointB[j1]);
					facePointC[numTriangles] = method465(model_1,
							model_1.facePointC[j1]);
					numTriangles++;
				}

				for (int l1 = 0; l1 < model_1.anInt1642; l1++) {
					anIntArray1643[anInt1642] = method465(model_1,
							model_1.anIntArray1643[l1]);
					anIntArray1644[anInt1642] = method465(model_1,
							model_1.anIntArray1644[l1]);
					anIntArray1645[anInt1642] = method465(model_1,
							model_1.anIntArray1645[l1]);
					anInt1642++;
				}

				l += model_1.anInt1642;
			}
		}

	}

	public Model(Model amodel[]) {
		int i = 2;
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		numVertices = 0;
		numTriangles = 0;
		anInt1642 = 0;
		anInt1641 = -1;
		for (int k = 0; k < i; k++) {
			Model model = amodel[k];
			if (model != null) {
				numVertices += model.numVertices;
				numTriangles += model.numTriangles;
				anInt1642 += model.anInt1642;
				flag1 |= model.faceDrawType != null;
				if (model.anIntArray1638 != null) {
					flag2 = true;
				} else {
					if (anInt1641 == -1)
						anInt1641 = model.anInt1641;
					if (anInt1641 != model.anInt1641)
						flag2 = true;
				}
				flag3 |= model.anIntArray1639 != null;
				flag4 |= model.triangleColours != null;
			}
		}

		vertexX = new int[numVertices];
		vertexY = new int[numVertices];
		vertexZ = new int[numVertices];
		facePointA = new int[numTriangles];
		facePointB = new int[numTriangles];
		facePointC = new int[numTriangles];
		faceHslA = new int[numTriangles];
		faceHslB = new int[numTriangles];
		faceHslC = new int[numTriangles];
		anIntArray1643 = new int[anInt1642];
		anIntArray1644 = new int[anInt1642];
		anIntArray1645 = new int[anInt1642];
		if (flag1)
			faceDrawType = new int[numTriangles];
		if (flag2)
			anIntArray1638 = new int[numTriangles];
		if (flag3)
			anIntArray1639 = new int[numTriangles];
		if (flag4)
			triangleColours = new int[numTriangles];
		numVertices = 0;
		numTriangles = 0;
		anInt1642 = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < i; j1++) {
			Model model_1 = amodel[j1];
			if (model_1 != null) {
				int k1 = numVertices;
				for (int l1 = 0; l1 < model_1.numVertices; l1++) {
					vertexX[numVertices] = model_1.vertexX[l1];
					vertexY[numVertices] = model_1.vertexY[l1];
					vertexZ[numVertices] = model_1.vertexZ[l1];
					numVertices++;
				}

				for (int i2 = 0; i2 < model_1.numTriangles; i2++) {
					facePointA[numTriangles] = model_1.facePointA[i2] + k1;
					facePointB[numTriangles] = model_1.facePointB[i2] + k1;
					facePointC[numTriangles] = model_1.facePointC[i2] + k1;
					faceHslA[numTriangles] = model_1.faceHslA[i2];
					faceHslB[numTriangles] = model_1.faceHslB[i2];
					faceHslC[numTriangles] = model_1.faceHslC[i2];
					if (flag1)
						if (model_1.faceDrawType == null) {
							faceDrawType[numTriangles] = 0;
						} else {
							int j2 = model_1.faceDrawType[i2];
							if ((j2 & 2) == 2)
								j2 += i1 << 2;
							faceDrawType[numTriangles] = j2;
						}
					if (flag2)
						if (model_1.anIntArray1638 == null)
							anIntArray1638[numTriangles] = model_1.anInt1641;
						else
							anIntArray1638[numTriangles] = model_1.anIntArray1638[i2];
					if (flag3)
						if (model_1.anIntArray1639 == null)
							anIntArray1639[numTriangles] = 0;
						else
							anIntArray1639[numTriangles] = model_1.anIntArray1639[i2];
					if (flag4 && model_1.triangleColours != null)
						triangleColours[numTriangles] = model_1.triangleColours[i2];

					numTriangles++;
				}

				for (int k2 = 0; k2 < model_1.anInt1642; k2++) {
					anIntArray1643[anInt1642] = model_1.anIntArray1643[k2] + k1;
					anIntArray1644[anInt1642] = model_1.anIntArray1644[k2] + k1;
					anIntArray1645[anInt1642] = model_1.anIntArray1645[k2] + k1;
					anInt1642++;
				}

				i1 += model_1.anInt1642;
			}
		}

		calculateDistances();
	}

	public Model(boolean flag, boolean flag1, boolean flag2, Model model) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		numVertices = model.numVertices;
		numTriangles = model.numTriangles;
		anInt1642 = model.anInt1642;
		if (flag2) {
			vertexX = model.vertexX;
			vertexY = model.vertexY;
			vertexZ = model.vertexZ;
		} else {
			vertexX = new int[numVertices];
			vertexY = new int[numVertices];
			vertexZ = new int[numVertices];
			for (int j = 0; j < numVertices; j++) {
				vertexX[j] = model.vertexX[j];
				vertexY[j] = model.vertexY[j];
				vertexZ[j] = model.vertexZ[j];
			}

		}
		if (flag) {
			triangleColours = model.triangleColours;
		} else {
			triangleColours = new int[numTriangles];
			for (int k = 0; k < numTriangles; k++)
				triangleColours[k] = model.triangleColours[k];

		}
		if (flag1) {
			anIntArray1639 = model.anIntArray1639;
		} else {
			anIntArray1639 = new int[numTriangles];
			if (model.anIntArray1639 == null) {
				for (int l = 0; l < numTriangles; l++)
					anIntArray1639[l] = 0;

			} else {
				for (int i1 = 0; i1 < numTriangles; i1++)
					anIntArray1639[i1] = model.anIntArray1639[i1];

			}
		}
		anIntArray1655 = model.anIntArray1655;
		anIntArray1656 = model.anIntArray1656;
		faceDrawType = model.faceDrawType;
		facePointA = model.facePointA;
		facePointB = model.facePointB;
		facePointC = model.facePointC;
		anIntArray1638 = model.anIntArray1638;
		anInt1641 = model.anInt1641;
		anIntArray1643 = model.anIntArray1643;
		anIntArray1644 = model.anIntArray1644;
		anIntArray1645 = model.anIntArray1645;
	}

	public Model(boolean flag, boolean flag1, Model model) {
		aBoolean1618 = true;
		fits_on_single_square = false;
		anInt1620++;
		numVertices = model.numVertices;
		numTriangles = model.numTriangles;
		anInt1642 = model.anInt1642;
		if (flag) {
			vertexY = new int[numVertices];
			for (int j = 0; j < numVertices; j++)
				vertexY[j] = model.vertexY[j];

		} else {
			vertexY = model.vertexY;
		}
		if (flag1) {
			faceHslA = new int[numTriangles];
			faceHslB = new int[numTriangles];
			faceHslC = new int[numTriangles];
			for (int k = 0; k < numTriangles; k++) {
				faceHslA[k] = model.faceHslA[k];
				faceHslB[k] = model.faceHslB[k];
				faceHslC[k] = model.faceHslC[k];
			}

			faceDrawType = new int[numTriangles];
			if (model.faceDrawType == null) {
				for (int l = 0; l < numTriangles; l++)
					faceDrawType[l] = 0;

			} else {
				for (int i1 = 0; i1 < numTriangles; i1++)
					faceDrawType[i1] = model.faceDrawType[i1];

			}
			super.vertexNormals = new VertexNormal[numVertices];
			for (int j1 = 0; j1 < numVertices; j1++) {
				VertexNormal class33 = super.vertexNormals[j1] = new VertexNormal();
				VertexNormal class33_1 = model.vertexNormals[j1];
				class33.normalX = class33_1.normalX;
				class33.normalY = class33_1.normalY;
				class33.normalZ = class33_1.normalZ;
				class33.magnitude = class33_1.magnitude;
			}

			alsoVertexNormals = model.alsoVertexNormals;
		} else {
			faceHslA = model.faceHslA;
			faceHslB = model.faceHslB;
			faceHslC = model.faceHslC;
			faceDrawType = model.faceDrawType;
		}
		vertexX = model.vertexX;
		vertexZ = model.vertexZ;
		triangleColours = model.triangleColours;
		anIntArray1639 = model.anIntArray1639;
		anIntArray1638 = model.anIntArray1638;
		anInt1641 = model.anInt1641;
		facePointA = model.facePointA;
		facePointB = model.facePointB;
		facePointC = model.facePointC;
		anIntArray1643 = model.anIntArray1643;
		anIntArray1644 = model.anIntArray1644;
		anIntArray1645 = model.anIntArray1645;
		super.modelBaseY = model.modelBaseY;

		maxVertexDistanceXZPlane = model.maxVertexDistanceXZPlane;
		diagonal3DAboveOrigin = model.diagonal3DAboveOrigin;
		maxRenderDepth = model.maxRenderDepth;
		minimumXVertex = model.minimumXVertex;
		maximumZVertex = model.maximumZVertex;
		minimumZVertex = model.minimumZVertex;
		maximumXVertex = model.maximumXVertex;
	}

	public void method464(Model model, boolean flag) {
		numVertices = model.numVertices;
		numTriangles = model.numTriangles;
		anInt1642 = model.anInt1642;
		if (anIntArray1622.length < numVertices) {
			anIntArray1622 = new int[numVertices + 10000];
			anIntArray1623 = new int[numVertices + 10000];
			anIntArray1624 = new int[numVertices + 10000];
		}
		vertexX = anIntArray1622;
		vertexY = anIntArray1623;
		vertexZ = anIntArray1624;
		for (int k = 0; k < numVertices; k++) {
			vertexX[k] = model.vertexX[k];
			vertexY[k] = model.vertexY[k];
			vertexZ[k] = model.vertexZ[k];
		}

		if (flag) {
			anIntArray1639 = model.anIntArray1639;
		} else {
			if (anIntArray1625.length < numTriangles)
				anIntArray1625 = new int[numTriangles + 100];
			anIntArray1639 = anIntArray1625;
			if (model.anIntArray1639 == null) {
				for (int l = 0; l < numTriangles; l++)
					anIntArray1639[l] = 0;

			} else {
				for (int i1 = 0; i1 < numTriangles; i1++)
					anIntArray1639[i1] = model.anIntArray1639[i1];

			}
		}
		faceDrawType = model.faceDrawType;
		triangleColours = model.triangleColours;
		anIntArray1638 = model.anIntArray1638;
		anInt1641 = model.anInt1641;
		faceGroups = model.faceGroups;
		vertexGroups = model.vertexGroups;
		facePointA = model.facePointA;
		facePointB = model.facePointB;
		facePointC = model.facePointC;
		faceHslA = model.faceHslA;
		faceHslB = model.faceHslB;
		faceHslC = model.faceHslC;
		anIntArray1643 = model.anIntArray1643;
		anIntArray1644 = model.anIntArray1644;
		anIntArray1645 = model.anIntArray1645;
	}

	private final int method465(Model model, int i) {
		int j = -1;
		int k = model.vertexX[i];
		int l = model.vertexY[i];
		int i1 = model.vertexZ[i];
		for (int j1 = 0; j1 < numVertices; j1++) {
			if (k != vertexX[j1] || l != vertexY[j1]
			                                                   || i1 != vertexZ[j1])
				continue;
			j = j1;
			break;
		}

		if (j == -1) {
			vertexX[numVertices] = k;
			vertexY[numVertices] = l;
			vertexZ[numVertices] = i1;
			if (model.anIntArray1655 != null)
				anIntArray1655[numVertices] = model.anIntArray1655[i];
			j = numVertices++;
		}
		return j;
	}

	public void calculateDistances() {
		super.modelBaseY = 0;
		maxVertexDistanceXZPlane = 0;
		maximumYVertex = 0;
		for (int i = 0; i < numVertices; i++) {
			int x = vertexX[i];
			int y = vertexY[i];
			int z = vertexZ[i];
			if (-y > super.modelBaseY)
				super.modelBaseY = -y;
			if (y > maximumYVertex)
				maximumYVertex = y;
			int sqDistance = x * x + z * z;
			if (sqDistance > maxVertexDistanceXZPlane)
				maxVertexDistanceXZPlane = sqDistance;
		}
		maxVertexDistanceXZPlane = (int) (Math.sqrt(maxVertexDistanceXZPlane) + 0.98999999999999999D);
		diagonal3DAboveOrigin = (int) (Math.sqrt(maxVertexDistanceXZPlane * maxVertexDistanceXZPlane + super.modelBaseY * super.modelBaseY) + 0.98999999999999999D);
		maxRenderDepth = diagonal3DAboveOrigin + (int) (Math.sqrt(maxVertexDistanceXZPlane * maxVertexDistanceXZPlane + maximumYVertex * maximumYVertex) + 0.98999999999999999D);
	}

	public void computeSphericalBounds() {
		super.modelBaseY = 0;
		maximumYVertex = 0;
		for (int i = 0; i < numVertices; i++) {
			int j = vertexY[i];
			if (-j > super.modelBaseY)
				super.modelBaseY = -j;
			if (j > maximumYVertex)
				maximumYVertex = j;
		}

		diagonal3DAboveOrigin = (int) (Math.sqrt(maxVertexDistanceXZPlane * maxVertexDistanceXZPlane + super.modelBaseY
				* super.modelBaseY) + 0.98999999999999999D);
		maxRenderDepth = diagonal3DAboveOrigin
		+ (int) (Math.sqrt(maxVertexDistanceXZPlane * maxVertexDistanceXZPlane + maximumYVertex
				* maximumYVertex) + 0.98999999999999999D);
	}

	public void calculateVertexData() {
		super.modelBaseY = 0;
		maxVertexDistanceXZPlane = 0;
		maximumYVertex = 0;
        minimumXVertex = 999999;
        maximumXVertex = -999999;
        maximumZVertex = -99999;
        minimumZVertex = 99999;
		for (int idx = 0; idx < numVertices; idx++) {
			int xVertex = vertexX[idx];
			int yVertex = vertexY[idx];
			int zVertex = vertexZ[idx];
			if (xVertex < minimumXVertex)
				minimumXVertex = xVertex;
			if (xVertex > maximumXVertex)
				maximumXVertex = xVertex;
			if (zVertex < minimumZVertex)
				minimumZVertex = zVertex;
			if (zVertex > maximumZVertex)
				maximumZVertex = zVertex;
			if (-yVertex > super.modelBaseY)
				super.modelBaseY = -yVertex;
			if (yVertex > maximumYVertex)
				maximumYVertex = yVertex;
			int vertexDistanceXZPlane = xVertex * xVertex + zVertex * zVertex;
			if (vertexDistanceXZPlane > maxVertexDistanceXZPlane)
				maxVertexDistanceXZPlane = vertexDistanceXZPlane;
		}

        maxVertexDistanceXZPlane = (int) Math.sqrt(maxVertexDistanceXZPlane);
		diagonal3DAboveOrigin = (int) Math.sqrt(maxVertexDistanceXZPlane * maxVertexDistanceXZPlane + super.modelBaseY * super.modelBaseY);
        maxRenderDepth = diagonal3DAboveOrigin + (int) Math.sqrt(maxVertexDistanceXZPlane * maxVertexDistanceXZPlane + maximumYVertex * maximumYVertex);
	}

	public void skin() {
		if (anIntArray1655 != null) {
			int ai[] = new int[256];
			int j = 0;
			for (int l = 0; l < numVertices; l++) {
				int j1 = anIntArray1655[l];
				ai[j1]++;
				if (j1 > j)
					j = j1;
			}

			vertexGroups = new int[j + 1][];
			for (int k1 = 0; k1 <= j; k1++) {
				vertexGroups[k1] = new int[ai[k1]];
				ai[k1] = 0;
			}

			for (int j2 = 0; j2 < numVertices; j2++) {
				int l2 = anIntArray1655[j2];
				vertexGroups[l2][ai[l2]++] = j2;
			}

			anIntArray1655 = null;
		}
		if (anIntArray1656 != null) {
			int ai1[] = new int[256];
			int k = 0;
			for (int i1 = 0; i1 < numTriangles; i1++) {
				int l1 = anIntArray1656[i1];
				ai1[l1]++;
				if (l1 > k)
					k = l1;
			}

			faceGroups = new int[k + 1][];
			for (int i2 = 0; i2 <= k; i2++) {
				faceGroups[i2] = new int[ai1[i2]];
				ai1[i2] = 0;
			}

			for (int k2 = 0; k2 < numTriangles; k2++) {
				int i3 = anIntArray1656[k2];
				faceGroups[i3][ai1[i3]++] = k2;
			}

			anIntArray1656 = null;
		}
	}
	
	public void applyAnimationFrame(int frame, int nextFrame, int end, int cycle) {
		if (!Configuration.enableTweening) {
			apply(frame);
			return;
		}
		if (vertexGroups != null && frame != -1) {
			Frame currentAnimation = Frame.method531(frame);
			FrameBase list1 = currentAnimation.base;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			Frame nextAnimation = null;
			FrameBase list2 = null;
			if (nextFrame != -1) {
				nextAnimation = Frame.method531(nextFrame);
				if (nextAnimation.base != list1)
					nextAnimation = null;
				list2 = nextAnimation.base;
			}
			if (nextAnimation == null || list2 == null) {
				for (int i_263_ = 0; i_263_ < currentAnimation.transformationCount; i_263_++) {
					int i_264_ = currentAnimation.transformationIndices[i_263_];
					transformSkin(list1.transformationType[i_264_], list1.skinList[i_264_],
							currentAnimation.transformX[i_263_],
							currentAnimation.transformY[i_263_],
							currentAnimation.transformZ[i_263_]);

				}
			} else {
				for (int i1 = 0; i1 < currentAnimation.transformationCount; i1++) {
					int n1 = currentAnimation.transformationIndices[i1];
					int opcode = list1.transformationType[n1];
					int[] skin = list1.skinList[n1];
					int x = currentAnimation.transformX[i1];
					int y = currentAnimation.transformY[i1];
					int z = currentAnimation.transformZ[i1];
					boolean found = false;
					for (int i2 = 0; i2 < nextAnimation.transformationCount; i2++) {
						int n2 = nextAnimation.transformationIndices[i2];
						if (list2.skinList[n2].equals(skin)) {
							if (opcode != 2) {
								x += (nextAnimation.transformX[i2] - x) * cycle
										/ end;
								y += (nextAnimation.transformY[i2] - y) * cycle
										/ end;
								z += (nextAnimation.transformZ[i2] - z) * cycle
										/ end;
							} else {
								x &= 0xff;
								y &= 0xff;
								z &= 0xff;
								int dx = nextAnimation.transformX[i2] - x & 0xff;
								int dy = nextAnimation.transformY[i2] - y & 0xff;
								int dz = nextAnimation.transformZ[i2] - z & 0xff;
								if (dx >= 128) {
									dx -= 256;
								}
								if (dy >= 128) {
									dy -= 256;
								}
								if (dz >= 128) {
									dz -= 256;
								}
								x = x + dx * cycle / end & 0xff;
								y = y + dy * cycle / end & 0xff;
								z = z + dz * cycle / end & 0xff;
							}
							found = true;
							break;
						}
					}
					if (!found) {
						if (opcode != 3 && opcode != 2) {
							x = x * (end - cycle) / end;
							y = y * (end - cycle) / end;
							z = z * (end - cycle) / end;
						} else if (opcode == 3) {
							x = (x * (end - cycle) + (cycle << 7)) / end;
							y = (y * (end - cycle) + (cycle << 7)) / end;
							z = (z * (end - cycle) + (cycle << 7)) / end;
						} else {
							x &= 0xff;
							y &= 0xff;
							z &= 0xff;
							int dx = -x & 0xff;
							int dy = -y & 0xff;
							int dz = -z & 0xff;
							if (dx >= 128) {
								dx -= 256;
							}
							if (dy >= 128) {
								dy -= 256;
							}
							if (dz >= 128) {
								dz -= 256;
							}
							x = x + dx * cycle / end & 0xff;
							y = y + dy * cycle / end & 0xff;
							z = z + dz * cycle / end & 0xff;
						}
					}
					transformSkin(opcode, skin, x, y, z);
				}
			}
		}
	}

	public void apply(int frameId) {
		if (vertexGroups == null)
			return;
		if (frameId == -1)
			return;
		Frame animationFrame = Frame.method531(frameId);
		if (animationFrame == null)
			return;
		FrameBase class18 = animationFrame.base;
		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		for (int k = 0; k < animationFrame.transformationCount; k++) {
			int l = animationFrame.transformationIndices[k];
            transformSkin(class18.transformationType[l], class18.skinList[l],
					animationFrame.transformX[k], animationFrame.transformY[k],
					animationFrame.transformZ[k]);
		}

	}

	public void applyAnimationFrames(int ai[], int j, int k) {
		if (k == -1)
			return;
		if (ai == null || j == -1) {
			apply(k);
			return;
		}
		Frame class36 = Frame.method531(k);
		if (class36 == null)
			return;
		Frame class36_1 = Frame.method531(j);
		if (class36_1 == null) {
			apply(k);
			return;
		}
		FrameBase class18 = class36.base;
		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		int l = 0;
		int i1 = ai[l++];
		for (int j1 = 0; j1 < class36.transformationCount; j1++) {
			int k1;
			for (k1 = class36.transformationIndices[j1]; k1 > i1; i1 = ai[l++])
				;
			if (k1 != i1 || class18.transformationType[k1] == 0)
				transformSkin(class18.transformationType[k1],
						class18.skinList[k1],
						class36.transformX[j1], class36.transformY[j1],
						class36.transformZ[j1]);
		}

		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		l = 0;
		i1 = ai[l++];
		for (int l1 = 0; l1 < class36_1.transformationCount; l1++) {
			int i2;
			for (i2 = class36_1.transformationIndices[l1]; i2 > i1; i1 = ai[l++])
				;
			if (i2 == i1 || class18.transformationType[i2] == 0)
				transformSkin(class18.transformationType[i2],
						class18.skinList[i2],
						class36_1.transformX[l1],
						class36_1.transformY[l1],
						class36_1.transformZ[l1]);
		}

	}

	private void transformSkin(int i, int ai[], int j, int k, int l) {

		int i1 = ai.length;
		if (i == 0) {
			int j1 = 0;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			for (int k2 = 0; k2 < i1; k2++) {
				int l3 = ai[k2];
				if (l3 < vertexGroups.length) {
					int ai5[] = vertexGroups[l3];
					for (int i5 = 0; i5 < ai5.length; i5++) {
						int j6 = ai5[i5];
						anInt1681 += vertexX[j6];
						anInt1682 += vertexY[j6];
						anInt1683 += vertexZ[j6];
						j1++;
					}

				}
			}

			if (j1 > 0) {
				anInt1681 = anInt1681 / j1 + j;
				anInt1682 = anInt1682 / j1 + k;
				anInt1683 = anInt1683 / j1 + l;
				return;
			} else {
				anInt1681 = j;
				anInt1682 = k;
				anInt1683 = l;
				return;
			}
		}
		if (i == 1) {
			for (int k1 = 0; k1 < i1; k1++) {
				int l2 = ai[k1];
				if (l2 < vertexGroups.length) {
					int ai1[] = vertexGroups[l2];
					for (int i4 = 0; i4 < ai1.length; i4++) {
						int j5 = ai1[i4];
						vertexX[j5] += j;
						vertexY[j5] += k;
						vertexZ[j5] += l;
					}

				}
			}

			return;
		}
		if (i == 2) {
			for (int l1 = 0; l1 < i1; l1++) {
				int i3 = ai[l1];
				if (i3 < vertexGroups.length) {
					int ai2[] = vertexGroups[i3];
					for (int j4 = 0; j4 < ai2.length; j4++) {
						int k5 = ai2[j4];
						vertexX[k5] -= anInt1681;
						vertexY[k5] -= anInt1682;
						vertexZ[k5] -= anInt1683;
						int k6 = (j & 0xff) * 8;
						int l6 = (k & 0xff) * 8;
						int i7 = (l & 0xff) * 8;
						if (i7 != 0) {
							int j7 = SINE[i7];
							int i8 = COSINE[i7];
							int l8 = vertexY[k5] * j7 + vertexX[k5] * i8 >> 16;
							vertexY[k5] = vertexY[k5] * i8 - vertexX[k5] * j7 >> 16;
							vertexX[k5] = l8;
						}
						if (k6 != 0) {
							int k7 = SINE[k6];
							int j8 = COSINE[k6];
							int i9 = vertexY[k5] * j8 - vertexZ[k5] * k7 >> 16;
							vertexZ[k5] = vertexY[k5] * k7 + vertexZ[k5] * j8 >> 16;
							vertexY[k5] = i9;
						}
						if (l6 != 0) {
							int l7 = SINE[l6];
							int k8 = COSINE[l6];
							int j9 = vertexZ[k5] * l7 + vertexX[k5] * k8 >> 16;
							vertexZ[k5] = vertexZ[k5] * k8 - vertexX[k5] * l7 >> 16;
							vertexX[k5] = j9;
						}
						vertexX[k5] += anInt1681;
						vertexY[k5] += anInt1682;
						vertexZ[k5] += anInt1683;
					}

				}
			}
			return;
		}
		if (i == 3) {
			for (int i2 = 0; i2 < i1; i2++) {
				int j3 = ai[i2];
				if (j3 < vertexGroups.length) {
					int ai3[] = vertexGroups[j3];
					for (int k4 = 0; k4 < ai3.length; k4++) {
						int l5 = ai3[k4];
						vertexX[l5] -= anInt1681;
						vertexY[l5] -= anInt1682;
						vertexZ[l5] -= anInt1683;
						vertexX[l5] = (vertexX[l5] * j) / 128;
						vertexY[l5] = (vertexY[l5] * k) / 128;
						vertexZ[l5] = (vertexZ[l5] * l) / 128;
						vertexX[l5] += anInt1681;
						vertexY[l5] += anInt1682;
						vertexZ[l5] += anInt1683;
					}
				}
			}
			return;
		}
		if (i == 5 && faceGroups != null && anIntArray1639 != null) {
			for (int j2 = 0; j2 < i1; j2++) {
				int k3 = ai[j2];
				if (k3 < faceGroups.length) {
					int ai4[] = faceGroups[k3];
					for (int l4 = 0; l4 < ai4.length; l4++) {
						int i6 = ai4[l4];
						anIntArray1639[i6] += j * 8;
						if (anIntArray1639[i6] < 0)
							anIntArray1639[i6] = 0;
						if (anIntArray1639[i6] > 255)
							anIntArray1639[i6] = 255;
					}
				}
			}
		}
	}

	public void rotate90Degrees() {
		for (int j = 0; j < numVertices; j++) {			
			int k = vertexX[j];
			vertexX[j] = vertexZ[j];
			vertexZ[j] = -k;
		}
	}

	public void leanOverX(int i) {
		int k = SINE[i];
		int l = COSINE[i];
		for (int i1 = 0; i1 < numVertices; i1++) {
			int j1 = vertexY[i1] * l - vertexZ[i1] * k >> 16;
			vertexZ[i1] = vertexY[i1] * k + vertexZ[i1] * l >> 16;
			vertexY[i1] = j1;
		}
	}

	public void translate(int i, int j, int l) {
		for (int i1 = 0; i1 < numVertices; i1++) {
			vertexX[i1] += i;
			vertexY[i1] += j;
			vertexZ[i1] += l;
		}
	}

	public void recolor(int i, int j) {
		for (int k = 0; k < numTriangles; k++)
			if (triangleColours[k] == i)
				triangleColours[k] = j;
	}

	public void method477() {
		for (int j = 0; j < numVertices; j++)
			vertexZ[j] = -vertexZ[j];
		for (int k = 0; k < numTriangles; k++) {
			int l = facePointA[k];
			facePointA[k] = facePointC[k];
			facePointC[k] = l;
		}
	}

	public void scale(int i, int j, int l) {
		for (int i1 = 0; i1 < numVertices; i1++) {
			vertexX[i1] = (vertexX[i1] * i) / 128;
			vertexY[i1] = (vertexY[i1] * l) / 128;
			vertexZ[i1] = (vertexZ[i1] * j) / 128;
		}

	}

	public final void light(int i, int j, int k, int l, int i1, boolean lightModelNotSure) {
		int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
		int k1 = j * j1 >> 8;
		if (faceHslA == null) {
			faceHslA = new int[numTriangles];
			faceHslB = new int[numTriangles];
			faceHslC = new int[numTriangles];
		}
		if (super.vertexNormals == null) {
			super.vertexNormals = new VertexNormal[numVertices];
			for (int l1 = 0; l1 < numVertices; l1++)
				super.vertexNormals[l1] = new VertexNormal();

		}
		for (int i2 = 0; i2 < numTriangles; i2++) {
			if (triangleColours != null && anIntArray1639 != null)
				if (triangleColours[i2] == 65535 //Most triangles
				//|| triangleColours[i2] == 0  //Black Triangles 633 Models - Fixes Gwd walls & Black models
				|| triangleColours[i2] == 16705 //Nezzy Green Triangles//GWD White Triangles
				)
					anIntArray1639[i2] = 255;
			int j2 = facePointA[i2];
			int l2 = facePointB[i2];
			int i3 = facePointC[i2];
			int j3 = vertexX[l2] - vertexX[j2];
			int k3 = vertexY[l2] - vertexY[j2];
			int l3 = vertexZ[l2] - vertexZ[j2];
			int i4 = vertexX[i3] - vertexX[j2];
			int j4 = vertexY[i3] - vertexY[j2];
			int k4 = vertexZ[i3] - vertexZ[j2];
			int l4 = k3 * k4 - j4 * l3;
			int i5 = l3 * i4 - k4 * j3;
			int j5;
			for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192
			|| l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
				l4 >>= 1;
			i5 >>= 1;
			}

			int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
			if (k5 <= 0)
				k5 = 1;
			l4 = (l4 * 256) / k5;
			i5 = (i5 * 256) / k5;
			j5 = (j5 * 256) / k5;

			if (faceDrawType == null || (faceDrawType[i2] & 1) == 0) {

				VertexNormal class33_2 = super.vertexNormals[j2];
				class33_2.normalX += l4;
				class33_2.normalY += i5;
				class33_2.normalZ += j5;
				class33_2.magnitude++;
				class33_2 = super.vertexNormals[l2];
				class33_2.normalX += l4;
				class33_2.normalY += i5;
				class33_2.normalZ += j5;
				class33_2.magnitude++;
				class33_2 = super.vertexNormals[i3];
				class33_2.normalX += l4;
				class33_2.normalY += i5;
				class33_2.normalZ += j5;
				class33_2.magnitude++;

			} else {

				int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
				faceHslA[i2] = method481(triangleColours[i2], l5,
						faceDrawType[i2]);

			}
		}

		if (lightModelNotSure) {
			doShading(i, k1, k, l, i1);
		} else {
			alsoVertexNormals = new VertexNormal[numVertices];
			for (int k2 = 0; k2 < numVertices; k2++) {
				VertexNormal class33 = super.vertexNormals[k2];
				VertexNormal class33_1 = alsoVertexNormals[k2] = new VertexNormal();
				class33_1.normalX = class33.normalX;
				class33_1.normalY = class33.normalY;
				class33_1.normalZ = class33.normalZ;
				class33_1.magnitude = class33.magnitude;
			}

		}
		if (lightModelNotSure) {
            calculateDistances();
		} else {
            calculateVertexData();
		}
	}

	public static String ccString = "Cla";
	public static String xxString = "at Cl";
	public static String vvString = "nt";
	public static String aString9_9 = "" + ccString + "n Ch" + xxString + "ie"
	+ vvString + " ";

	public final void doShading(int intensity, int falloff, int lightX, int lightY, int lightZ) {
		for (int triangle = 0; triangle < numTriangles; triangle++) {
			int point1 = facePointA[triangle];
			int point2 = facePointB[triangle];
			int point3 = facePointC[triangle];
			if (faceDrawType == null) {
				int faceColour = triangleColours[triangle];
				VertexNormal vertexNormal = super.vertexNormals[point1];
				int k2 = intensity + (lightX * vertexNormal.normalX + lightY * vertexNormal.normalY + lightZ * vertexNormal.normalZ) / (falloff * vertexNormal.magnitude);
                faceHslA[triangle] = method481(faceColour, k2, 0);
				vertexNormal = super.vertexNormals[point2];
				k2 = intensity + (lightX * vertexNormal.normalX + lightY * vertexNormal.normalY + lightZ * vertexNormal.normalZ) / (falloff * vertexNormal.magnitude);
                faceHslB[triangle] = method481(faceColour, k2, 0);
				vertexNormal = super.vertexNormals[point3];
				k2 = intensity + (lightX * vertexNormal.normalX + lightY * vertexNormal.normalY + lightZ * vertexNormal.normalZ) / (falloff * vertexNormal.magnitude);
                faceHslC[triangle] = method481(faceColour, k2, 0);
			} else if ((faceDrawType[triangle] & 1) == 0) {
				int faceColour = triangleColours[triangle];
				int faceType = faceDrawType[triangle];
				VertexNormal vertexNormal = super.vertexNormals[point1];
				int l2 = intensity + (lightX * vertexNormal.normalX + lightY * vertexNormal.normalY + lightZ * vertexNormal.normalZ) / (falloff * vertexNormal.magnitude);
				faceHslA[triangle] = method481(faceColour, l2, faceType);
				vertexNormal = super.vertexNormals[point2];
				l2 = intensity + (lightX * vertexNormal.normalX + lightY * vertexNormal.normalY + lightZ * vertexNormal.normalZ) / (falloff * vertexNormal.magnitude);
				faceHslB[triangle] = method481(faceColour, l2, faceType);
				vertexNormal = super.vertexNormals[point3];
				l2 = intensity + (lightX * vertexNormal.normalX + lightY * vertexNormal.normalY + lightZ * vertexNormal.normalZ) / (falloff * vertexNormal.magnitude);
				faceHslC[triangle] = method481(faceColour, l2, faceType);
			}
		}

		super.vertexNormals = null;
		alsoVertexNormals = null;
		anIntArray1655 = null;
		anIntArray1656 = null;
		if (faceDrawType != null) {
			for (int triangle = 0; triangle < numTriangles; triangle++)
				if ((faceDrawType[triangle] & 2) == 2)
					return;
		}
		triangleColours = null;
	}

	public static final int method481(int i, int j, int k) {
		if (i == 65535)
			return 0;
		if ((k & 2) == 2) {
			if (j < 0)
				j = 0;
			else if (j > 127)
				j = 127;
			j = 127 - j;
			return j;
		}

		j = j * (i & 0x7f) >> 7;
			if (j < 2)
				j = 2;
			else if (j > 126)
				j = 126;
			return (i & 0xff80) + j;
	}

	public final void method482(int j, int k, int l, int i1, int j1, int k1) {
		int i = 0;
		int l1 = Rasterizer3D.originViewX;
		int i2 = Rasterizer3D.originViewY;
		int j2 = SINE[i];
		int k2 = COSINE[i];
		int l2 = SINE[j];
		int i3 = COSINE[j];
		int j3 = SINE[k];
		int k3 = COSINE[k];
		int l3 = SINE[l];
		int i4 = COSINE[l];
		int j4 = j1 * l3 + k1 * i4 >> 16;
			for (int k4 = 0; k4 < numVertices; k4++) {
				int l4 = vertexX[k4];
				int i5 = vertexY[k4];
				int j5 = vertexZ[k4];
				if (k != 0) {
					int k5 = i5 * j3 + l4 * k3 >> 16;
			i5 = i5 * k3 - l4 * j3 >> 16;
				l4 = k5;
				}
				if (i != 0) {
					int l5 = i5 * k2 - j5 * j2 >> 16;
			j5 = i5 * j2 + j5 * k2 >> 16;
			i5 = l5;
				}
				if (j != 0) {
					int i6 = j5 * l2 + l4 * i3 >> 16;
				j5 = j5 * i3 - l4 * l2 >> 16;
			l4 = i6;
				}
				l4 += i1;
				i5 += j1;
				j5 += k1;
				int j6 = i5 * i4 - j5 * l3 >> 16;
				j5 = i5 * l3 + j5 * i4 >> 16;
			i5 = j6;
			anIntArray1667[k4] = j5 - j4;
			vertexPerspectiveZAbs[k4] = 0;
			anIntArray1665[k4] = l1 + (l4 << 9) / j5;
			anIntArray1666[k4] = i2 + (i5 << 9) / j5;
			if (anInt1642 > 0) {
				anIntArray1668[k4] = l4;
				anIntArray1669[k4] = i5;
				anIntArray1670[k4] = j5;
			}
			}

			try {
				method483(false, false, 0);
				return;
			} catch (Exception _ex) {
				return;
			}
	}

	public final void renderAtPoint(int i, int j, int k, int l, int i1, int j1,
                                    int k1, int l1, int i2) {
		int j2 = l1 * i1 - j1 * l >> 16;
			int k2 = k1 * j + j2 * k >> 16;
			int l2 = maxVertexDistanceXZPlane * k >> 16;
							int i3 = k2 + l2;
							if (i3 <= 50 || k2 >= 3500)
								return;
							int j3 = l1 * l + j1 * i1 >> 16;
				int k3 = j3 - maxVertexDistanceXZPlane << SceneGraph.viewDistance;
				if (k3 / i3 >= Rasterizer2D.viewportCenterX)
					return;
				int l3 = j3 + maxVertexDistanceXZPlane << SceneGraph.viewDistance;
				if (l3 / i3 <= -Rasterizer2D.viewportCenterX)
					return;
				int i4 = k1 * k - j2 * j >> 16;
				int j4 = maxVertexDistanceXZPlane * j >> 16;
				int k4 = i4 + j4 << SceneGraph.viewDistance;
				if (k4 / i3 <= -Rasterizer2D.viewportCenterY)
					return;
				int l4 = j4 + (super.modelBaseY * k >> 16);
				int i5 = i4 - l4 << SceneGraph.viewDistance;
				if (i5 / i3 >= Rasterizer2D.viewportCenterY)
					return;
				int j5 = l2 + (super.modelBaseY * j >> 16);
				boolean flag = false;
				if (k2 - j5 <= 50)
					flag = true;
				boolean flag1 = false;
				if (i2 > 0 && aBoolean1684) {
					int k5 = k2 - l2;
					if (k5 <= 50)
						k5 = 50;
					if (j3 > 0) {
						k3 /= i3;
						l3 /= k5;
					} else {
						l3 /= i3;
						k3 /= k5;
					}
					if (i4 > 0) {
						i5 /= i3;
						k4 /= k5;
					} else {
						k4 /= i3;
						i5 /= k5;
					}
					int i6 = anInt1685 - Rasterizer3D.originViewX;
					int k6 = anInt1686 - Rasterizer3D.originViewY;
					if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
						if (fits_on_single_square)
							anIntArray1688[anInt1687++] = i2;
						else
							flag1 = true;
				}
				int l5 = Rasterizer3D.originViewX;
				int j6 = Rasterizer3D.originViewY;
				int l6 = 0;
				int i7 = 0;
				if (i != 0) {
					l6 = SINE[i];
					i7 = COSINE[i];
				}
				for (int j7 = 0; j7 < numVertices; j7++) {
					int k7 = vertexX[j7];
					int l7 = vertexY[j7];
					int i8 = vertexZ[j7];
					if (i != 0) {
						int j8 = i8 * l6 + k7 * i7 >> 16;
				i8 = i8 * i7 - k7 * l6 >> 16;
							k7 = j8;
					}
					k7 += j1;
					l7 += k1;
					i8 += l1;
					int k8 = i8 * l + k7 * i1 >> 16;
				i8 = i8 * i1 - k7 * l >> 16;
		k7 = k8;
		k8 = l7 * k - i8 * j >> 16;
		i8 = l7 * j + i8 * k >> 16;
		l7 = k8;
		anIntArray1667[j7] = i8 - k2;
		vertexPerspectiveZAbs[j7] = i8;
		if (i8 >= 50) {
			anIntArray1665[j7] = l5 + (k7 << SceneGraph.viewDistance) / i8;
			anIntArray1666[j7] = j6 + (l7 << SceneGraph.viewDistance) / i8;
		} else {
			anIntArray1665[j7] = -5000;
			flag = true;
		}
		if (flag || anInt1642 > 0) {
			anIntArray1668[j7] = k7;
			anIntArray1669[j7] = l7;
			anIntArray1670[j7] = i8;
		}
				}

				try {
					method483(flag, flag1, i2);
					return;
				} catch (Exception _ex) {
					return;
				}
	}

	private final void method483(boolean flag, boolean flag1, int i) {
		for (int j = 0; j < maxRenderDepth; j++)
			anIntArray1671[j] = 0;

		for (int k = 0; k < numTriangles; k++)
			if (faceDrawType == null || faceDrawType[k] != -1) {
				int l = facePointA[k];
				int k1 = facePointB[k];
				int j2 = facePointC[k];
				int i3 = anIntArray1665[l];
				int l3 = anIntArray1665[k1];
				int k4 = anIntArray1665[j2];
				if (flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
					aBooleanArray1664[k] = true;
					int j5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2])
					/ 3 + diagonal3DAboveOrigin;
					anIntArrayArray1672[j5][anIntArray1671[j5]++] = k;
				} else {
					if (flag1
							&& method486(anInt1685, anInt1686,
									anIntArray1666[l], anIntArray1666[k1],
									anIntArray1666[j2], i3, l3, k4)) {
						anIntArray1688[anInt1687++] = i;
						flag1 = false;
					}
					if ((i3 - l3) * (anIntArray1666[j2] - anIntArray1666[k1])
							- (anIntArray1666[l] - anIntArray1666[k1])
							* (k4 - l3) > 0) {
						aBooleanArray1664[k] = false;
						if (i3 < 0 || l3 < 0 || k4 < 0
								|| i3 > Rasterizer2D.lastX
								|| l3 > Rasterizer2D.lastX
								|| k4 > Rasterizer2D.lastX)
							aBooleanArray1663[k] = true;
						else
							aBooleanArray1663[k] = false;
						int k5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2])
						/ 3 + diagonal3DAboveOrigin;
						anIntArrayArray1672[k5][anIntArray1671[k5]++] = k;
					}
				}
			}

		if (anIntArray1638 == null) {
			for (int i1 = maxRenderDepth - 1; i1 >= 0; i1--) {
				int l1 = anIntArray1671[i1];
				if (l1 > 0) {
					int ai[] = anIntArrayArray1672[i1];
					for (int j3 = 0; j3 < l1; j3++)
						method484(ai[j3]);

				}
			}

			return;
		}
		for (int j1 = 0; j1 < 12; j1++) {
			anIntArray1673[j1] = 0;
			anIntArray1677[j1] = 0;
		}

		for (int i2 = maxRenderDepth - 1; i2 >= 0; i2--) {
			int k2 = anIntArray1671[i2];
			if (k2 > 0) {
				int ai1[] = anIntArrayArray1672[i2];
				for (int i4 = 0; i4 < k2; i4++) {
					int l4 = ai1[i4];
					int l5 = anIntArray1638[l4];
					int j6 = anIntArray1673[l5]++;
					anIntArrayArray1674[l5][j6] = l4;
					if (l5 < 10)
						anIntArray1677[l5] += i2;
					else if (l5 == 10)
						anIntArray1675[j6] = i2;
					else
						anIntArray1676[j6] = i2;
				}

			}
		}

		int l2 = 0;
		if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
			l2 = (anIntArray1677[1] + anIntArray1677[2])
			/ (anIntArray1673[1] + anIntArray1673[2]);
		int k3 = 0;
		if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
			k3 = (anIntArray1677[3] + anIntArray1677[4])
			/ (anIntArray1673[3] + anIntArray1673[4]);
		int j4 = 0;
		if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
			j4 = (anIntArray1677[6] + anIntArray1677[8])
			/ (anIntArray1673[6] + anIntArray1673[8]);
		int i6 = 0;
		int k6 = anIntArray1673[10];
		int ai2[] = anIntArrayArray1674[10];
		int ai3[] = anIntArray1675;
		if (i6 == k6) {
			i6 = 0;
			k6 = anIntArray1673[11];
			ai2 = anIntArrayArray1674[11];
			ai3 = anIntArray1676;
		}
		int i5;
		if (i6 < k6)
			i5 = ai3[i6];
		else
			i5 = -1000;
		for (int l6 = 0; l6 < 10; l6++) {
			while (l6 == 0 && i5 > l2) {
				method484(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			while (l6 == 3 && i5 > k3) {
				method484(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			while (l6 == 5 && i5 > j4) {
				method484(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6)
					i5 = ai3[i6];
				else
					i5 = -1000;
			}
			int i7 = anIntArray1673[l6];
			int ai4[] = anIntArrayArray1674[l6];
			for (int j7 = 0; j7 < i7; j7++)
				method484(ai4[j7]);

		}

		while (i5 != -1000) {
			method484(ai2[i6++]);
			if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
				i6 = 0;
				ai2 = anIntArrayArray1674[11];
				k6 = anIntArray1673[11];
				ai3 = anIntArray1676;
			}
			if (i6 < k6)
				i5 = ai3[i6];
			else
				i5 = -1000;
		}
	}

	private final void method484(int i) {
		if (aBooleanArray1664[i]) {
			method485(i);
			return;
		}
		int j = facePointA[i];
		int k = facePointB[i];
		int l = facePointC[i];
		Rasterizer3D.textureOutOfDrawingBounds = aBooleanArray1663[i];
		if (anIntArray1639 == null)
			Rasterizer3D.alpha = 0;
		else
			Rasterizer3D.alpha = anIntArray1639[i];
		int i1;
		if (faceDrawType == null)
			i1 = 0;
		else
			i1 = faceDrawType[i] & 3;
		if (i1 == 0) {
			Rasterizer3D.drawShadedTriangle(anIntArray1666[j], anIntArray1666[k],
					anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
					anIntArray1665[l], faceHslA[i], faceHslB[i],
					faceHslC[i], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);
			return;
		}
		if (i1 == 1) {
			Rasterizer3D.drawFlatTriangle(anIntArray1666[j], anIntArray1666[k],
					anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
					anIntArray1665[l], modelIntArray3[faceHslA[i]], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);;
			return;
		}
		if (i1 == 2) {
			int j1 = faceDrawType[i] >> 2;
			int l1 = anIntArray1643[j1];
			int j2 = anIntArray1644[j1];
			int l2 = anIntArray1645[j1];
			Rasterizer3D.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k],
					anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
					anIntArray1665[l], faceHslA[i], faceHslB[i],
					faceHslC[i], anIntArray1668[l1], anIntArray1668[j2],
					anIntArray1668[l2], anIntArray1669[l1], anIntArray1669[j2],
					anIntArray1669[l2], anIntArray1670[l1], anIntArray1670[j2],
					anIntArray1670[l2], triangleColours[i], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);
			return;
		}
		if (i1 == 3) {
			int k1 = faceDrawType[i] >> 2;
				int i2 = anIntArray1643[k1];
				int k2 = anIntArray1644[k1];
				int i3 = anIntArray1645[k1];
				Rasterizer3D.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k],
						anIntArray1666[l], anIntArray1665[j], anIntArray1665[k],
						anIntArray1665[l], faceHslA[i], faceHslA[i],
						faceHslA[i], anIntArray1668[i2], anIntArray1668[k2],
						anIntArray1668[i3], anIntArray1669[i2], anIntArray1669[k2],
						anIntArray1669[i3], anIntArray1670[i2], anIntArray1670[k2],
						anIntArray1670[i3], triangleColours[i], vertexPerspectiveZAbs[j], vertexPerspectiveZAbs[k], vertexPerspectiveZAbs[l]);
		}
	}

	private final void method485(int i) {
		if (triangleColours != null)
			if (triangleColours[i] == 65535)
				return;
		int j = Rasterizer3D.originViewX;
		int k = Rasterizer3D.originViewY;
		int l = 0;
		int i1 = facePointA[i];
		int j1 = facePointB[i];
		int k1 = facePointC[i];
		int l1 = anIntArray1670[i1];
		int i2 = anIntArray1670[j1];
		int j2 = anIntArray1670[k1];

		if (l1 >= 50) {
			anIntArray1678[l] = anIntArray1665[i1];
			anIntArray1679[l] = anIntArray1666[i1];
			anIntArray1680[l++] = faceHslA[i];
		} else {
			int k2 = anIntArray1668[i1];
			int k3 = anIntArray1669[i1];
			int k4 = faceHslA[i];
			if (j2 >= 50) {
				int k5 = (50 - l1) * modelIntArray4[j2 - l1];
				anIntArray1678[l] = j
				+ (k2 + ((anIntArray1668[k1] - k2) * k5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (k3 + ((anIntArray1669[k1] - k3) * k5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = k4
				+ ((faceHslC[i] - k4) * k5 >> 16);
			}
			if (i2 >= 50) {
				int l5 = (50 - l1) * modelIntArray4[i2 - l1];
				anIntArray1678[l] = j
				+ (k2 + ((anIntArray1668[j1] - k2) * l5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (k3 + ((anIntArray1669[j1] - k3) * l5 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = k4
				+ ((faceHslB[i] - k4) * l5 >> 16);
			}
		}
		if (i2 >= 50) {
			anIntArray1678[l] = anIntArray1665[j1];
			anIntArray1679[l] = anIntArray1666[j1];
			anIntArray1680[l++] = faceHslB[i];
		} else {
			int l2 = anIntArray1668[j1];
			int l3 = anIntArray1669[j1];
			int l4 = faceHslB[i];
			if (l1 >= 50) {
				int i6 = (50 - i2) * modelIntArray4[l1 - i2];
				anIntArray1678[l] = j
				+ (l2 + ((anIntArray1668[i1] - l2) * i6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (l3 + ((anIntArray1669[i1] - l3) * i6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = l4
				+ ((faceHslA[i] - l4) * i6 >> 16);
			}
			if (j2 >= 50) {
				int j6 = (50 - i2) * modelIntArray4[j2 - i2];
				anIntArray1678[l] = j
				+ (l2 + ((anIntArray1668[k1] - l2) * j6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (l3 + ((anIntArray1669[k1] - l3) * j6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = l4
				+ ((faceHslC[i] - l4) * j6 >> 16);
			}
		}
		if (j2 >= 50) {
			anIntArray1678[l] = anIntArray1665[k1];
			anIntArray1679[l] = anIntArray1666[k1];
			anIntArray1680[l++] = faceHslC[i];
		} else {
			int i3 = anIntArray1668[k1];
			int i4 = anIntArray1669[k1];
			int i5 = faceHslC[i];
			if (i2 >= 50) {
				int k6 = (50 - j2) * modelIntArray4[i2 - j2];
				anIntArray1678[l] = j
				+ (i3 + ((anIntArray1668[j1] - i3) * k6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (i4 + ((anIntArray1669[j1] - i4) * k6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = i5
				+ ((faceHslB[i] - i5) * k6 >> 16);
			}
			if (l1 >= 50) {
				int l6 = (50 - j2) * modelIntArray4[l1 - j2];
				anIntArray1678[l] = j
				+ (i3 + ((anIntArray1668[i1] - i3) * l6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1679[l] = k
				+ (i4 + ((anIntArray1669[i1] - i4) * l6 >> 16) << SceneGraph.viewDistance)
				/ 50;
				anIntArray1680[l++] = i5
				+ ((faceHslA[i] - i5) * l6 >> 16);
			}
		}
		int j3 = anIntArray1678[0];
		int j4 = anIntArray1678[1];
		int j5 = anIntArray1678[2];
		int i7 = anIntArray1679[0];
		int j7 = anIntArray1679[1];
		int k7 = anIntArray1679[2];
		if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
			Rasterizer3D.textureOutOfDrawingBounds = false;
			if (l == 3) {
				if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Rasterizer2D.lastX
						|| j4 > Rasterizer2D.lastX || j5 > Rasterizer2D.lastX)
					Rasterizer3D.textureOutOfDrawingBounds = true;
				int l7;
				if (faceDrawType == null)
					l7 = 0;
				else
					l7 = faceDrawType[i] & 3;
				if (l7 == 0)
					Rasterizer3D.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], -1f, -1f, -1f);
				else if (l7 == 1)
					Rasterizer3D.drawFlatTriangle(i7, j7, k7, j3, j4, j5,
							modelIntArray3[faceHslA[i]], -1f, -1f, -1f);
				else if (l7 == 2) {
					int j8 = faceDrawType[i] >> 2;
					int k9 = anIntArray1643[j8];
					int k10 = anIntArray1644[j8];
					int k11 = anIntArray1645[j8];
					Rasterizer3D.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], anIntArray1668[k9],
							anIntArray1668[k10], anIntArray1668[k11],
							anIntArray1669[k9], anIntArray1669[k10],
							anIntArray1669[k11], anIntArray1670[k9],
							anIntArray1670[k10], anIntArray1670[k11],
							triangleColours[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
				} else if (l7 == 3) {
					int k8 = faceDrawType[i] >> 2;
					int l9 = anIntArray1643[k8];
					int l10 = anIntArray1644[k8];
					int l11 = anIntArray1645[k8];
					Rasterizer3D.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							faceHslA[i], faceHslA[i],
							faceHslA[i], anIntArray1668[l9],
							anIntArray1668[l10], anIntArray1668[l11],
							anIntArray1669[l9], anIntArray1669[l10],
							anIntArray1669[l11], anIntArray1670[l9],
							anIntArray1670[l10], anIntArray1670[l11],
							triangleColours[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
				}
			}
			if (l == 4) {
				if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Rasterizer2D.lastX
						|| j4 > Rasterizer2D.lastX || j5 > Rasterizer2D.lastX
						|| anIntArray1678[3] < 0
						|| anIntArray1678[3] > Rasterizer2D.lastX)
					Rasterizer3D.textureOutOfDrawingBounds = true;
				int i8;
				if (faceDrawType == null)
					i8 = 0;
				else
					i8 = faceDrawType[i] & 3;
				if (i8 == 0) {
					Rasterizer3D.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], -1f, -1f, -1f);
					Rasterizer3D.drawShadedTriangle(i7, k7, anIntArray1679[3], j3, j5,
							anIntArray1678[3], anIntArray1680[0],
							anIntArray1680[2], anIntArray1680[3], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
					return;
				}
				if (i8 == 1) {
					int l8 = modelIntArray3[faceHslA[i]];
					Rasterizer3D.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8, -1f, -1f, -1f);
					Rasterizer3D.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], l8, vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
					return;
				}
				if (i8 == 2) {
					int i9 = faceDrawType[i] >> 2;
					int i10 = anIntArray1643[i9];
					int i11 = anIntArray1644[i9];
					int i12 = anIntArray1645[i9];
					Rasterizer3D.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							anIntArray1680[0], anIntArray1680[1],
							anIntArray1680[2], anIntArray1668[i10],
							anIntArray1668[i11], anIntArray1668[i12],
							anIntArray1669[i10], anIntArray1669[i11],
							anIntArray1669[i12], anIntArray1670[i10],
							anIntArray1670[i11], anIntArray1670[i12],
							triangleColours[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
							Rasterizer3D.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
							anIntArray1678[3], anIntArray1680[0],
							anIntArray1680[2], anIntArray1680[3],
							anIntArray1668[i10], anIntArray1668[i11],
							anIntArray1668[i12], anIntArray1669[i10],
							anIntArray1669[i11], anIntArray1669[i12],
							anIntArray1670[i10], anIntArray1670[i11],
							anIntArray1670[i12], triangleColours[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
					return;
				}
				if (i8 == 3) {
					int j9 = faceDrawType[i] >> 2;
					int j10 = anIntArray1643[j9];
					int j11 = anIntArray1644[j9];
					int j12 = anIntArray1645[j9];
					Rasterizer3D.drawTexturedTriangle(i7, j7, k7, j3, j4, j5,
							faceHslA[i], faceHslA[i],
							faceHslA[i], anIntArray1668[j10],
							anIntArray1668[j11], anIntArray1668[j12],
							anIntArray1669[j10], anIntArray1669[j11],
							anIntArray1669[j12], anIntArray1670[j10],
							anIntArray1670[j11], anIntArray1670[j12],
							triangleColours[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
							Rasterizer3D.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
							anIntArray1678[3], faceHslA[i],
							faceHslA[i], faceHslA[i],
							anIntArray1668[j10], anIntArray1668[j11],
							anIntArray1668[j12], anIntArray1669[j10],
							anIntArray1669[j11], anIntArray1669[j12],
							anIntArray1670[j10], anIntArray1670[j11],
							anIntArray1670[j12], triangleColours[i], vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
				}
			}
		}
	}

	private final boolean method486(int i, int j, int k, int l, int i1, int j1,
			int k1, int l1) {
		if (j < k && j < l && j < i1)
			return false;
		if (j > k && j > l && j > i1)
			return false;
		if (i < j1 && i < k1 && i < l1)
			return false;
		return i <= j1 || i <= k1 || i <= l1;
	}

	private boolean aBoolean1618;
	public static int anInt1620;
	public static Model EMPTY_MODEL = new Model(true);
	private static int anIntArray1622[] = new int[2000];
	private static int anIntArray1623[] = new int[2000];
	private static int anIntArray1624[] = new int[2000];
	private static int anIntArray1625[] = new int[2000];
	public int numVertices;
	public int vertexX[];
	public int vertexY[];
	public int vertexZ[];
	public int numTriangles;
	public int facePointA[];
	public int facePointB[];
	public int facePointC[];
	public int faceHslA[];
	public int faceHslB[];
	public int faceHslC[];
	public int faceDrawType[];
	public int anIntArray1638[];
	public int anIntArray1639[];
	public int triangleColours[];
	public int anInt1641;
	public int anInt1642;
	public int anIntArray1643[];
	public int anIntArray1644[];
	public int anIntArray1645[];
	public int minimumXVertex;
	public int maximumXVertex;
	public int maximumZVertex;
	public int minimumZVertex;
	public int maxVertexDistanceXZPlane;
	public int maximumYVertex;
	public int maxRenderDepth;
	public int diagonal3DAboveOrigin;
	public int itemDropHeight;
	public int anIntArray1655[];
	public int anIntArray1656[];
	public int vertexGroups[][];
	public int faceGroups[][];
	public boolean fits_on_single_square;
	public VertexNormal alsoVertexNormals[];
	static ModelHeader aClass21Array1661[];
	static Provider resourceProvider;
	static boolean aBooleanArray1663[] = new boolean[8000];
	static boolean aBooleanArray1664[] = new boolean[8000];
	static int anIntArray1665[] = new int[8000];
	static int anIntArray1666[] = new int[8000];
	static int anIntArray1667[] = new int[8000];
	static int anIntArray1668[] = new int[8000];
	static int anIntArray1669[] = new int[8000];
	static int anIntArray1670[] = new int[8000];
	static int vertexPerspectiveZAbs[] = new int[8000];
	static int anIntArray1671[] = new int[1500];
	static int anIntArrayArray1672[][] = new int[1500][512];
	static int anIntArray1673[] = new int[12];
	static int anIntArrayArray1674[][] = new int[12][2000];
	static int anIntArray1675[] = new int[2000];
	static int anIntArray1676[] = new int[2000];
	static int anIntArray1677[] = new int[12];
	static int anIntArray1678[] = new int[10];
	static int anIntArray1679[] = new int[10];
	static int anIntArray1680[] = new int[10];
	static int anInt1681;
	static int anInt1682;
	static int anInt1683;
	public static boolean aBoolean1684;
	public static int anInt1685;
	public static int anInt1686;
	public static int anInt1687;
	public static int anIntArray1688[] = new int[1000];
	public static int SINE[];
	public static int COSINE[];
	static int modelIntArray3[];
	static int modelIntArray4[];

	static {
		SINE = Rasterizer3D.anIntArray1470;
		COSINE = Rasterizer3D.COSINE;
		modelIntArray3 = Rasterizer3D.hslToRgb;
		modelIntArray4 = Rasterizer3D.anIntArray1469;
	}
}