package astraeus.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

import astraeus.game.model.entity.mob.player.collect.Equipment.EquipmentDefinition;
import astraeus.util.GsonObjectParser;

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
