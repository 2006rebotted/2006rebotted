package rs2.game.model.entity;

import java.util.LinkedList;

import rs2.game.model.Position;
import rs2.game.model.entity.mob.Mob;
import rs2.game.task.Task;

/**
 * Represents an entity in the game world.
 * 
 * @author Seven
 */
public abstract class Entity {

	private final LinkedList<Task> tasks = new LinkedList<Task>();

	public abstract int size();
	
	public abstract EntityType type();
	
	protected Position position;

	public boolean isMob() {
		return getClass() == Mob.class;
	}

	public Mob getMob() {
		return (Mob) this;
	}

	public int getX() {
		return position.getX();
	}

	public int getY() {
		return position.getY();
	}

	public Position getPosition() {
		return position;		
	}

	public void setPosition(Position position) {		
		this.position = position;
	}

	public LinkedList<Task> getTasks() {
		return tasks;
	}

}
