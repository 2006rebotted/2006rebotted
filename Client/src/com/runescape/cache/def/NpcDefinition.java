package com.runescape.cache.def;

import com.runescape.Client;
import com.runescape.cache.FileArchive;
import com.runescape.cache.anim.Frame;
import com.runescape.cache.config.VariableBits;
import com.runescape.collection.ReferenceCache;
import com.runescape.entity.model.Model;
import com.runescape.io.Buffer;

/**
 * Refactored reference:
 * http://www.rune-server.org/runescape-development/rs2-client/downloads/575183-almost-fully-refactored-317-client.html
 */
public final class NpcDefinition {

	public int turn90CCWAnimIndex;
	public static int anInt56;
	public int varBitID;
	public int turn180AnimIndex;
	public int settingId;
	public static Buffer dataBuf;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte size;
	public int[] recolourTarget;
	public static int[] offsets;
	public int[] aditionalModels;
	public int headIcon;
	public int[] recolourOriginal;
	public int standAnim;
	public long interfaceType;
	public int degreesToTurn;
	public static NpcDefinition[] cache;
	public static Client clientInstance;
	public int turn90CWAnimIndex;
	public boolean clickable;
	public int lightModifier;
	public int scaleY;
	public boolean drawMinimapDot;
	public int childrenIDs[];
	public byte description[];
	public int scaleXZ;
	public int shadowModifier;
	public boolean priorityRender;
	public int[] modelId;
	public static ReferenceCache modelCache = new ReferenceCache(30);

	/**
	 * Lookup an NpcDefinition by its id
	 * @param id
	 */
	public static NpcDefinition lookup(int id) {
		for (int index = 0; index < 20; index++)
			if (cache[index].interfaceType == (long) id)
				return cache[index];

		anInt56 = (anInt56 + 1) % 20;
		NpcDefinition definition = cache[anInt56] = new NpcDefinition();
		dataBuf.currentPosition = offsets[id];
		definition.interfaceType = id;
		definition.readValues(dataBuf);
		return definition;
	}

	public Model model() {
		if (childrenIDs != null) {
			NpcDefinition entityDef = morph();
			if (entityDef == null)
				return null;
			else
				return entityDef.model();
		}
		if (aditionalModels == null)
			return null;
		boolean flag1 = false;
		for (int index = 0; index < aditionalModels.length; index++)
			if (!Model.isCached(aditionalModels[index]))
				flag1 = true;

		if (flag1)
			return null;
		Model models[] = new Model[aditionalModels.length];
		for (int index = 0; index < aditionalModels.length; index++)
			models[index] = Model.getModel(aditionalModels[index]);

		Model model;
		if (models.length == 1)
			model = models[0];
		else
			model = new Model(models.length, models);
		if (recolourOriginal != null) {
			for (int index = 0; index < recolourOriginal.length; index++)
				model.recolor(recolourOriginal[index], recolourTarget[index]);

		}
		return model;
	}

	public NpcDefinition morph() {
		int child = -1;
		if (varBitID != -1) {
			VariableBits varBit = VariableBits.varbits[varBitID];
			int variable = varBit.getSetting();
			int low = varBit.getLow();
			int high = varBit.getHigh();
			int mask = Client.BIT_MASKS[high - low];
			child = clientInstance.settings[variable] >> low & mask;
		} else if (settingId != -1)
			child = clientInstance.settings[settingId];
		if (child < 0 || child >= childrenIDs.length
				|| childrenIDs[child] == -1)
			return null;
		else
			return lookup(childrenIDs[child]);
	}

	public static void init(FileArchive archive) {		
		dataBuf = new Buffer(archive.readFile("npc.dat"));		
		Buffer idxBuf = new Buffer(archive.readFile("npc.idx"));		
		
		int size = idxBuf.readUShort();		
		
		offsets = new int[size];		
		
		int offset = 2;
		
		for (int count = 0; count < size; count++) {
			offsets[count] = offset;
			offset += idxBuf.readUShort();
		}

		cache = new NpcDefinition[20];
		
		for (int count = 0; count < 20; count++) {
			cache[count] = new NpcDefinition();
		}		
		
		System.out.println("Loaded: " + size + " Npcs");
	}

	public static void clear() {
		modelCache = null;
		offsets = null;
		cache = null;
		dataBuf = null;
	}

	public Model method164(int j, int somethingCurrentAnimsFrameNumber, int ai[], int nextFrame, int idk,
			int idk2) {
		if (childrenIDs != null) {
			NpcDefinition entityDef = morph();
			if (entityDef == null)
				return null;
			else
				return entityDef.method164(j, somethingCurrentAnimsFrameNumber, ai, nextFrame, idk, idk2);
		}
		Model model = (Model) modelCache.get(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < modelId.length; i1++)
				if (!Model.isCached(modelId[i1]))
					flag = true;

			if (flag)
				return null;
			Model models[] = new Model[modelId.length];
			for (int j1 = 0; j1 < modelId.length; j1++)
				models[j1] = Model.getModel(modelId[j1]);

			if (models.length == 1)
				model = models[0];
			else
				model = new Model(models.length, models);
			if (recolourOriginal != null) {
				for (int k1 = 0; k1 < recolourOriginal.length; k1++)
					model.recolor(recolourOriginal[k1], recolourTarget[k1]);

			}
			model.skin();
			model.scale(132, 132, 132);
			model.light(84 + lightModifier, 1000 + shadowModifier, -90, -580,
					-90, true);
			modelCache.put(model, interfaceType);
		}
		Model empty = Model.EMPTY_MODEL;
		empty.method464(model,
				Frame.noAnimationInProgress(somethingCurrentAnimsFrameNumber) & Frame.noAnimationInProgress(j)
						& Frame.noAnimationInProgress(nextFrame));
		if (somethingCurrentAnimsFrameNumber != -1 && j != -1)
			empty.applyAnimationFrames(ai, j, somethingCurrentAnimsFrameNumber);
		else if (somethingCurrentAnimsFrameNumber != -1 && nextFrame != -1)
			empty.applyAnimationFrame(somethingCurrentAnimsFrameNumber, nextFrame, idk, idk2);
		else if (somethingCurrentAnimsFrameNumber != -1)
			empty.apply(somethingCurrentAnimsFrameNumber);
		if (scaleXZ != 128 || scaleY != 128)
			empty.scale(scaleXZ, scaleXZ, scaleY);
		empty.calculateDistances();
		empty.faceGroups = null;
		empty.vertexGroups = null;
		if (size == 1)
			empty.fits_on_single_square = true;
		return empty;
	}

	public Model getAnimatedModel(int primaryFrame, int secondaryFrame,
			int interleaveOrder[]) {
		if (childrenIDs != null) {
			NpcDefinition definition = morph();
			if (definition == null)
				return null;
			else
				return definition.getAnimatedModel(primaryFrame,
						secondaryFrame, interleaveOrder);
		}
		Model model = (Model) modelCache.get(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int index = 0; index < modelId.length; index++)
				if (!Model.isCached(modelId[index]))
					flag = true;

			if (flag)
				return null;
			Model models[] = new Model[modelId.length];
			for (int index = 0; index < modelId.length; index++)
				models[index] = Model.getModel(modelId[index]);

			if (models.length == 1)
				model = models[0];
			else
				model = new Model(models.length, models);
			if (recolourOriginal != null) {
				for (int index = 0; index < recolourOriginal.length; index++)
					model.recolor(recolourOriginal[index],
							recolourTarget[index]);

			}
			model.skin();
			model.light(64 + lightModifier, 850 + shadowModifier, -30, -50,
					-30, true);
			modelCache.put(model, interfaceType);
		}
		Model model_1 = Model.EMPTY_MODEL;
		model_1.method464(model, Frame.noAnimationInProgress(secondaryFrame)
				& Frame.noAnimationInProgress(primaryFrame));
		if (secondaryFrame != -1 && primaryFrame != -1)
			model_1.applyAnimationFrames(interleaveOrder, primaryFrame, secondaryFrame);
		else if (secondaryFrame != -1)
			model_1.apply(secondaryFrame);
		if (scaleXZ != 128 || scaleY != 128)
			model_1.scale(scaleXZ, scaleXZ, scaleY);
		model_1.calculateDistances();
		model_1.faceGroups = null;
		model_1.vertexGroups = null;
		if (size == 1)
			model_1.fits_on_single_square = true;
		return model_1;
	}

	public void readValues(Buffer stream) {
		do {
			int opCode = stream.readUnsignedByte();
			if (opCode == 0)
				return;
			if (opCode == 1) {
				int j = stream.readUnsignedByte();
				modelId = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					modelId[j1] = stream.readUShort();

			} else if (opCode == 2)
				name = stream.readNewString();
			else if (opCode == 3)
				description = stream.readBytes();
			else if (opCode == 12)
			      size = stream.readSignedByte();
			else if (opCode == 13)
				standAnim = stream.readUShort();
			else if (opCode == 14)
				walkAnim = stream.readUShort();
			else if (opCode == 17) {
				walkAnim = stream.readUShort();
				turn180AnimIndex = stream.readUShort();
				turn90CWAnimIndex = stream.readUShort();
				turn90CCWAnimIndex = stream.readUShort();
			} else if (opCode >= 30 && opCode < 40) {
				if (actions == null)
					actions = new String[5];
				actions[opCode - 30] = stream.readNewString();
				if (actions[opCode - 30].equalsIgnoreCase("hidden"))
					actions[opCode - 30] = null;
			} else if (opCode == 40) {
				int colours = stream.readUnsignedByte();
				recolourOriginal = new int[colours];
				recolourTarget = new int[colours];
				for (int k1 = 0; k1 < colours; k1++) {
					recolourOriginal[k1] = stream.readUShort();
					recolourTarget[k1] = stream.readUShort();
				}

			} else if (opCode == 60) {
				int additionalModelLen = stream.readUnsignedByte();
				aditionalModels = new int[additionalModelLen];
				for (int l1 = 0; l1 < additionalModelLen; l1++)
					aditionalModels[l1] = stream.readUShort();

			} else if (opCode == 90)
				stream.readUShort();
			else if (opCode == 91)
				stream.readUShort();
			else if (opCode == 92)
				stream.readUShort();
			else if (opCode == 93)
				drawMinimapDot = false;
			else if (opCode == 95)
				combatLevel = stream.readUShort();
			else if (opCode == 97)
				scaleXZ = stream.readUShort();
			else if (opCode == 98)
				scaleY = stream.readUShort();
			else if (opCode == 99)
				priorityRender = true;
			else if (opCode == 100)
				lightModifier = stream.readSignedByte();
			else if (opCode == 101)
				shadowModifier = stream.readSignedByte() * 5;
			else if (opCode == 102)
				headIcon = stream.readUShort();
			else if (opCode == 103)
				degreesToTurn = stream.readUShort();
			else if (opCode == 106) {
				varBitID = stream.readUShort();
				if (varBitID == 65535)
					varBitID = -1;
				settingId = stream.readUShort();
				if (settingId == 65535)
					settingId = -1;
				int childCount = stream.readUnsignedByte();
				childrenIDs = new int[childCount + 1];
				for (int i2 = 0; i2 <= childCount; i2++) {
					childrenIDs[i2] = stream.readUShort();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}

			} else if (opCode == 107)
				clickable = false;
		} while (true);
	}

	public NpcDefinition() {
		turn90CCWAnimIndex = -1;
		varBitID = -1;
		turn180AnimIndex = -1;
		settingId = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		size = 1;
		headIcon = -1;
		standAnim = -1;
		interfaceType = -1L;
		degreesToTurn = 32;
		turn90CWAnimIndex = -1;
		clickable = true;
		scaleY = 128;
		drawMinimapDot = true;
		scaleXZ = 128;
		priorityRender = false;
	}
}
