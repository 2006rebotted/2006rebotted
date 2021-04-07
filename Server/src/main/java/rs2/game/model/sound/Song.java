package rs2.game.model.sound;

/**
 * Represents a single song that can be played in-game.
 * 
 * @author Seven
 */
public final class Song {

	/**
	 * The id of this song.
	 */
	private final int id;

	/**
	 * The name of this song.
	 */
	private final String name;

	/**
	 * The regions this song is found in.
	 */
	private final int[] region;

	/**
	 * Creates a new {@link Song}.
	 * 
	 * @param id
	 *            The id of this song.
	 * 
	 * @param name
	 *            The name of this song.
	 * 
	 * @param region
	 *            The regions this song is played in.
	 */
	public Song(int id, String name, int[] region) {
		this.id = id;
		this.name = name;
		this.region = region;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the region
	 */
	public int[] getRegion() {
		return region;
	}

}
