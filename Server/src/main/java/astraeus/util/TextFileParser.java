package astraeus.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * A simple parser designed for text files.
 *
 * @author Seven
 */
public abstract class TextFileParser extends GenericParser {

	/**
	 * Creates a new {@link TextFileParser}.
	 *
	 * @param path
	 *      The path of the file to parse.
	 */
	public TextFileParser(String path) {
		this(path, true);
	}

	/**
	 * Creates a new {@link TextFileParser}.
	 *
	 * @param path
	 *      The path of the file to parse.
	 *
	 * @param log
	 * 		The flag that denotes to log messages.
	 */
	public TextFileParser(String path, boolean log) {
		super(path, ".txt", log);
	}

	/**
	 * The method called when the file is being parsed.
	 *
	 * @param reader
	 *      The underlying parser.
	 */
	public abstract void parse(Scanner reader) throws IOException;

	@Override
	public void deserialize() {
		try(Scanner reader = new Scanner(new FileReader(path.toFile()))) {
			while(reader.hasNextLine()) {
				parse(reader);
				index++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
