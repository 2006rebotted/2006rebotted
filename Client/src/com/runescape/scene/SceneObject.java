package com.runescape.scene;
import com.runescape.Client;
import com.runescape.cache.anim.Animation;
import com.runescape.cache.config.VariableBits;
import com.runescape.cache.def.ObjectDefinition;
import com.runescape.entity.Renderable;
import com.runescape.entity.model.Model;

public final class SceneObject extends Renderable {
	
	private int anInt1599;
	private final int[] anIntArray1600;
	private final int anInt1601;
	private final int anInt1602;
	private final int anInt1603;
	private final int anInt1604;
	private final int anInt1605;
	private final int anInt1606;
	private Animation aAnimation_1607;
	private int anInt1608;
	public static Client clientInstance;
	private final int anInt1610;
	private final int anInt1611;
	private final int anInt1612;

	private ObjectDefinition method457() {
		int i = -1;
		if (anInt1601 != -1) {
			try {
				VariableBits varBit = VariableBits.varbits[anInt1601];
				int k = varBit.getSetting();
				int l = varBit.getLow();
				int i1 = varBit.getHigh();
				int j1 = Client.BIT_MASKS[i1 - l];
				i = clientInstance.settings[k] >> l & j1;
			} catch (Exception ex) {
			}
		} else if (anInt1602 != -1) {
			i = clientInstance.settings[anInt1602];
		}
		if (i < 0 || i >= anIntArray1600.length || anIntArray1600[i] == -1) {
			return null;
		} else {
			return ObjectDefinition.lookup(anIntArray1600[i]);
		}
	}

	public Model getRotatedModel() {
		int j = -1;
		if (aAnimation_1607 != null) {
			int k = Client.tick - anInt1608;
			if (k > 100 && aAnimation_1607.loopOffset > 0) {
				k = 100;
			}
			while (k > aAnimation_1607.duration(anInt1599)) {
				k -= aAnimation_1607.duration(anInt1599);
				anInt1599++;
				if (anInt1599 < aAnimation_1607.frameCount)
					continue;
				anInt1599 -= aAnimation_1607.loopOffset;
				if (anInt1599 >= 0 && anInt1599 < aAnimation_1607.frameCount)
					continue;
				aAnimation_1607 = null;
				break;
			}
			anInt1608 = Client.tick - k;
			if (aAnimation_1607 != null) {
				j = aAnimation_1607.primaryFrames[anInt1599];
			}
		}
		ObjectDefinition class46;
		if (anIntArray1600 != null)
			class46 = method457();
		else
			class46 = ObjectDefinition.lookup(anInt1610);
		if (class46 == null) {
			return null;
		} else {
			return class46.modelAt(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, j);
		}
	}

	public SceneObject(int i, int j, int k, int l, int i1, int j1, int k1, int l1, boolean flag) {
		anInt1610 = i;
		anInt1611 = k;
		anInt1612 = j;
		anInt1603 = j1;
		anInt1604 = l;
		anInt1605 = i1;
		anInt1606 = k1;
		if (l1 != -1) {
			aAnimation_1607 = Animation.animations[l1];
			anInt1599 = 0;
			anInt1608 = Client.tick;
			if (flag && aAnimation_1607.loopOffset != -1) {
				anInt1599 = (int) (Math.random() * (double) aAnimation_1607.frameCount);
				anInt1608 -= (int) (Math.random() * (double) aAnimation_1607.duration(anInt1599));
			}
		}
		ObjectDefinition objectDef = ObjectDefinition.lookup(anInt1610);
		anInt1601 = objectDef.varbit;
		anInt1602 = objectDef.varp;
		anIntArray1600 = objectDef.childrenIDs;
	}
}