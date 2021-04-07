package astraeus.game.task.impl;

import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.task.Task;

/**
 * The {@link Task} implementation that involves a {@link Player} and a {@link Position}.
 * 
 * @author Vult-R
 */
public abstract class DistancedTask extends Task {
	
	/**
	 * The flag that denotes the destination has been reached.
	 */
	private boolean reached = false;

	/**
	 * The player that is performing this task.
	 */
	private final Player player;

	/**
	 * The ending destination
	 */
	private final Position destination;

	/**
	 * The distance in game tiles before the task can execute an action.
	 */
	private final int distance;
	
	/**
	 * Creates a new {@link DistancedTask}.
	 * 
	 * @param start
	 * 		The starting position
	 * 
	 * @param destination
	 * 		The ending position
	 * 
	 * @param distance
	 * 		The distance in tiles before {@link #onReached()} can execute.
	 */
	public DistancedTask(Player player, Position destination, int distance) {
		this(player, destination, distance, 0, true);
	}

	/**
	 * Creates a new {@link DistancedTask}.
	 * 
	 * @param start
	 * 		The starting position
	 * 
	 * @param destination
	 * 		The ending position
	 * 
	 * @param distance
	 * 		The distance in tiles before {@link #onReached()} can execute.
	 * 
	 * @param delay
	 * 		The delay in game ticks the task sleeps for.
	 */
	public DistancedTask(Player player, Position destination, int distance, int delay) {
		this(player, destination, distance, delay, true);
	}

	/**
	 * Creates a new {@link DistancedTask}.
	 * 
	 * @param start
	 * 		The starting position
	 * 
	 * @param destination
	 * 		The ending position
	 * 
	 * @param distance
	 * 		The distance in tiles before {@link #onReached()} can execute.
	 * 
	 * @param immediate
	 * 		The flag that denotes to execute this task immediately instead of being queued.
	 */
	public DistancedTask(Player player, Position destination, int distance, boolean immediate) {
		this(player, destination, distance, 0, immediate);
	}

	/**
	 * Creates a new {@link DistancedTask}.
	 * 
	 * @param start
	 * 		The starting position
	 * 
	 * @param destination
	 * 		The ending position
	 * 
	 * @param distance
	 * 		The distance in tiles before {@link #onReached()} can execute.
	 * 
	 * @param delay
	 * 		The delay in game ticks the task sleeps for.
	 * 
	 * @param immediate
	 * 		The flag that denotes to execute this task immediately instead of being queued.
	 */
	public DistancedTask(Player player, Position destination, int distance, int delay, boolean immediate) {
		super(delay, immediate);
		this.player = player;
		this.destination = destination;
		this.distance = distance;		
	}
	
	/**
	 * The method that executes the task.
	 */
	public void execute() {		
		int currentDistance = Position.getDistance(player.getPosition(), destination);
		
		System.out.println("test: " + currentDistance + " " + distance);
		
		if (reached) {
			onReached();
			stop();
		} else if (currentDistance <= distance) {
			reached = true;
			
			if (immediate) {
	             onReached();
	                stop();
			}
		}
	}

	/**
	 * The method called when the entity is in reach of its destination.
	 */
	public abstract void onReached();

	/**
	 * Gets the player performing this task.
	 */
	public final Player getPlayer() {
		return player;
	}

	/**
	 * Gets the destination
	 */
	public final Position getDestination() {
		return destination;
	}

	/**
	 * Gets the distance required for {@link #onReached()} to execute.
	 */
	public final int getDistance() {
		return distance;
	}

}
