package rs2.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

import rs2.game.model.entity.mob.npc.NpcSpawn;
import rs2.game.model.entity.mob.npc.Npcs;
import rs2.util.GsonObjectParser;

public final class NpcSpawnParser extends GsonObjectParser<NpcSpawn> {

	public NpcSpawnParser() {
		super("./data/npc/npc_spawns");
	}

	@Override
	public NpcSpawn[] deserialize(Gson gson, FileReader reader) throws IOException {
		return gson.fromJson(reader, NpcSpawn[].class);
	}

	@Override
	public void onRead(NpcSpawn[] array) throws IOException {
		Arrays.stream(array).forEach($it -> Npcs.createSpawn($it));		
	}

}
