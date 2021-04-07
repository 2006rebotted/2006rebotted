package rs2.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

import rs2.game.model.entity.object.GameObject;
import rs2.game.model.entity.object.GameObjectType;
import rs2.game.model.entity.object.GameObjects;
import rs2.game.model.entity.object.impl.GlobalObject;
import rs2.util.GsonObjectParser;

public final class GlobalObjectParser extends GsonObjectParser<GlobalObject> {

	public GlobalObjectParser() {
		super("./data/object/global_objects");
	}

	@Override
	public GlobalObject[] deserialize(Gson gson, FileReader reader) throws IOException {
		return gson.fromJson(reader, GlobalObject[].class);
	}

	@Override
	public void onRead(GlobalObject[] array) throws IOException {		
		Arrays.stream(array).forEach($it -> GameObjects.getGlobalObjects().add(new GameObject($it.getId(), GameObjectType.lookup($it.getType()).get(), $it.getLocation(), $it.getOrientation())));
	}

}
