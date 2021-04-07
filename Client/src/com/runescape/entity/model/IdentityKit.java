package com.runescape.entity.model;

import com.runescape.cache.FileArchive;
import com.runescape.io.Buffer;

public final class IdentityKit {
	
	public static int length;
	public static IdentityKit kits[];
	public int part;
	private int[] bodyModels;
	private final int[] originalColors;
	private final int[] replacementColors;
	private final int[] headModels = { -1, -1, -1, -1, -1 };
	public boolean validStyle;
	
	private IdentityKit() {
		part = -1;
		originalColors = new int[6];
		replacementColors = new int[6];
		validStyle = false;
	}

	public static void init(FileArchive archive) {
		Buffer stream = new Buffer(archive.readFile("idk.dat"));
		length = stream.readUShort();
		if (kits == null)
			kits = new IdentityKit[length];
		for (int id = 0; id < length; id++) {
			if (kits[id] == null)
				kits[id] = new IdentityKit();
			kits[id].readValues(stream);
		}
	}

	private void readValues(Buffer buffer) {
		do {
			int opcode = buffer.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1)
				part = buffer.readUnsignedByte();
			else if (opcode == 2) {
				int count = buffer.readUnsignedByte();
				bodyModels = new int[count];
				for (int part = 0; part < count; part++)
					bodyModels[part] = buffer.readUShort();

			} else if (opcode == 3)
				validStyle = true;
			else if (opcode >= 40 && opcode < 50)
				originalColors[opcode - 40] = buffer.readUShort();
			else if (opcode >= 50 && opcode < 60)
				replacementColors[opcode - 50] = buffer.readUShort();
			else if (opcode >= 60 && opcode < 70)
				headModels[opcode - 60] = buffer.readUShort();
			else
				System.out.println("Error unrecognised config code: " + opcode);
		} while (true);
	}

	public boolean bodyLoaded() {
		if (bodyModels == null)
			return true;
		boolean ready = true;
		for (int part = 0; part < bodyModels.length; part++)
			if (!Model.isCached(bodyModels[part]))
				ready = false;

		return ready;
	}

	public Model bodyModel() {
		if (bodyModels == null)
			return null;
		Model models[] = new Model[bodyModels.length];
		for (int part = 0; part < bodyModels.length; part++)
			models[part] = Model.getModel(bodyModels[part]);

		Model model;
		if (models.length == 1)
			model = models[0];
		else
			model = new Model(models.length, models);
		for (int part = 0; part < 6; part++) {
			if (originalColors[part] == 0)
				break;
			model.recolor(originalColors[part], replacementColors[part]);
		}

		return model;
	}

	public boolean headLoaded() {
		boolean ready = true;
		for (int part = 0; part < 5; part++)
			if (headModels[part] != -1 && !Model.isCached(headModels[part]))
				ready = false;

		return ready;
	}

	public Model headModel() {
		Model models[] = new Model[5];
		int count = 0;
		for (int part = 0; part < 5; part++)
			if (headModels[part] != -1)
				models[count++] = Model.getModel(headModels[part]);

		Model model = new Model(count, models);
		for (int part = 0; part < 6; part++) {
			if (originalColors[part] == 0)
				break;
			model.recolor(originalColors[part], replacementColors[part]);
		}

		return model;
	}
}
