package astraeus.util;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class provides an easy to use google gson parser specifically designed for parsing JSON files.
 *
 * @author Seven
 */
public abstract class GsonParser extends GenericParser {

	/**
	 * The {@link Gson} object.
	 */
	protected Gson builder;

	/**
	 * Creates a new {@link GsonObjectParser}.
	 *
	 * @param path
	 * 		The specified path of the json file to parse.
	 */
	public GsonParser(String path) {
		this(path, true);
	}

	/**
	 * Creates a new {@link GsonObjectParser}.
	 *
	 * @param path
	 * 		The specified path of the json file to parse.
	 *
	 * @param log
	 * 		The flag that denotes to log messages.
	 */
	public GsonParser(String path, boolean log) {
		super(path, ".json", log);
		this.builder = new GsonBuilder().create();
	}

	/**
	 * The method allows a user to modify the data as its being parsed.
	 *
	 * @param data
	 * 		The {@link JsonObject} that contains all serialized information.
	 */
	protected abstract void parse(JsonObject data);

	@Override
	public final void deserialize() {
		try (FileReader reader = new FileReader(path.toFile())) {
			JsonParser parser = new JsonParser();
			JsonArray array = (JsonArray) parser.parse(reader);

			for (index = 0; index < array.size(); index++) {
				JsonObject data = (JsonObject) array.get(index);
				parse(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
