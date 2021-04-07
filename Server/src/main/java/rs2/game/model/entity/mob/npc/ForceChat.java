package rs2.game.model.entity.mob.npc;

public class ForceChat {

	public enum Type {
		SEQUENTIAL,

		RANDOM;
	}

	/**
	 * The id of the mob.
	 */
	private final int id;

	/**
	 * The name of the mob.
	 */
	private final String name;

	/**
	 * The type of force chat.
	 */
	private final Type type;

	/**
	 * The time in seconds to display the forced chat.
	 */
	private final int time;

	/**
	 * The lines of text the npc will say.
	 */
	private final String[] lines;

	public ForceChat(int id, String name, Type type, int time, String[] lines) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.time = time;
		this.lines = lines;
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
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @return the lines
	 */
	public String[] getLines() {
		return lines;
	}

}
