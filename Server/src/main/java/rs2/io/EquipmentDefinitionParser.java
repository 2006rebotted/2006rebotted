package rs2.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

import rs2.game.model.entity.mob.player.collect.Equipment.EquipmentDefinition;
import rs2.util.GsonObjectParser;

public final class EquipmentDefinitionParser extends GsonObjectParser<EquipmentDefinition> {

	public EquipmentDefinitionParser() {
		super("./data/equipment/equipment_definitions");
	}

	@Override
	public EquipmentDefinition[] deserialize(Gson gson, FileReader reader) throws IOException {
		return gson.fromJson(reader, EquipmentDefinition[].class);
	}

	@Override
	public void onRead(EquipmentDefinition[] array) throws IOException {
		Arrays.stream(array).forEach($it -> EquipmentDefinition.equipment_definitions.put($it.getId(), $it));		
	}

}
