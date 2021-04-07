package astraeus.game.model;

/**
 * Class that models a single animation used by an entity.
 * 
 * @author Vult-R
 */
public final class Animation implements Comparable<Animation> {

	/**
	 * The animation id to play
	 */
	private final int id;

	/**
	 * The delay before playing the animation
	 */
	private final int delay;

	/**
	 * The priority of the animation.
	 */
	private final Priority priority;

	/**
	 * Creates a new instance of the animation with a hidden delay of 0.
	 * 
	 * @param id
	 *            The id of the animation being used.
	 */
	public Animation(int id) {
		this(Priority.NORMAL, id, 0);
	}

	/**
	 * Creates a new instance of the animation with a specified delay.
	 * 
	 * @param id
	 *            The id of the animation being used.
	 * @param delay
	 *            The delay of the animation in seconds.
	 */
	public Animation(int id, int delay) {
		this(Priority.NORMAL, id, delay);
	}

	/**
	 * Creates a new instance of the animation with a hidden delay of 0.
	 * 
	 * @param id
	 *            The id of the animation being used.
	 */
	public Animation(Priority priority, int id) {
		this(priority, id, 0);
	}

	/**
	 * Creates a new instance of the animation with a specified delay.
	 * 
	 * @param priority
	 *            The priority level of the animation.
	 * @param id
	 *            The id of the animation being used.
	 * @param delay
	 *            The delay of the animation in seconds.
	 */
	public Animation(Priority priority, int id, int delay) {
		this.priority = priority;
		this.id = id;
		this.delay = delay;
	}

	@Override
	public int compareTo(Animation other) {
		if (other == null) {
			return 1;
		}

		return Integer.signum(other.priority.getPriority() - priority.getPriority());
	}

	/**
	 * Returns the animations delay.
	 * 
	 * @return
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Returns the animations id.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("ANIMATION[priority=%s, id=%s, delay=%s]", priority, id, delay);
	}

}
