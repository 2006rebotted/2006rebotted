package astraeus.util;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.logging.Logger;

/**
 * A specialized parser for parsing json files.
 * 
 * @author Vult-R
 */
public abstract class GsonObjectParser<T> implements Runnable {

	/**
	 * The single logger for this class.
	 */
	private static final Logger LOGGER = LoggerUtils.getLogger(GsonObjectParser.class);
	
	/**
	 * The single instance of Gson for this parser.
	 */
	private static final Gson GSON = new GsonBuilder().create();

	/**
	 * The path to the file being parsed.
	 */
	protected String path;

	/**
	 * The flag that denotes to log this result to the output stream.
	 */
	private boolean log;

	/**
	 * Creates a new {@link GsonObjectParser}.
	 * 
	 * @param path
	 * 		The path of the file being parsed.
	 */
	public GsonObjectParser(String path) {
		this(path, true);
	}

	/**
	 * Creates a new {@link GsonObjectParser}.
	 * 
	 * @param path
	 * 		The path of the file being parsed.
	 * 
	 * @param log
	 * 		The flag to log this result.
	 */
	public GsonObjectParser(String path, boolean log) {
		this.path = path;
		this.log = log;
	}

	/**
	 * The method that will deserialize the json file into a java object.
	 * 
	 * @param gson
	 * 		The gson object
	 * 
	 * @param reader
	 * 		The reader that will parse the file.
	 * 
	 * @throws IOException
	 * 
	 * @return The type as an array.
	 */
	public abstract T[] deserialize(Gson gson, FileReader reader) throws IOException;

	/**
	 * The method called after the {@link #deserialize(Gson, FileReader)} has been called.
	 * 
	 * @param array
	 * 		The array that was deserialized.
	 * 
	 * @throw IOException
	 */
	public abstract void onRead(T[] array) throws IOException;

	@Override
	public void run() {		
		try (FileReader reader = new FileReader(path + ".json")) {
			
			T[] types = deserialize(GSON, reader);

			onRead(types);

			if (log) {
				LOGGER.info("Loaded: " + types.length + " " + path.substring(path.lastIndexOf("/")  + 1).replaceAll("_", " "));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
