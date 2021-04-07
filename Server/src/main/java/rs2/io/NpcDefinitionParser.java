package rs2.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

import rs2.game.model.entity.mob.npc.NpcDefinition;
import rs2.util.GsonObjectParser;

public final class NpcDefinitionParser extends GsonObjectParser<NpcDefinition> {

	public NpcDefinitionParser() {
		super("./Data/npc/npc_definitions");
	}

	@Override
	public NpcDefinition[] deserialize(Gson gson, FileReader reader) throws IOException {
		return gson.fromJson(reader, NpcDefinition[].class);		
	}

	@Override
	public void onRead(NpcDefinition[] array) throws IOException {
		Arrays.stream(array).forEach($it -> NpcDefinition.getDefinitions()[$it.getId()] = $it);		
	}

}
