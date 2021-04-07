package rs2.game.task.impl;

import rs2.game.model.entity.mob.Mob;
import rs2.game.task.Task;
import rs2.game.task.TaskType;

/**
 * Represents a task that involves a mob.
 * 
 * @author Vult-R
 */
public abstract class MobTask <T extends Mob> extends Task {
	
	/**
	 * The mob that this task is for.
	 */
	protected final T mob;
	
	/**
	 * Creates a new {@link MobTask}.
	 * 
	 * @param mob
	 * 		The mob that this task is for.
	 * 
	 * @param immediate
	 * 		The flag that denotes this task will execute immediately instead of being queued.
	 */
	public MobTask(T mob, boolean immediate) {		
		super(0, immediate, StackType.STACK, BreakType.NEVER, TaskType.CURRENT_ACTION);
		this.mob = mob;		
	}
	
	/**
	 * Creates a new {@link MobTask}.
	 * 
	 * @param mob
	 * 		The mob that this task is for.
	 * 
	 * @param delay
	 * 		The delay in game ticks that this task will sleep for.
	 */
	public MobTask(T mob, int delay) {		
		super(delay, false, StackType.STACK, BreakType.NEVER, TaskType.CURRENT_ACTION);
		this.mob = mob;		
	}
	
	/**
	 * Creates a new {@link MobTask}.
	 * 
	 * @param mob
	 * 		The mob that this task is for.
	 * 
	 * @param delay
	 * 		The delay in game ticks that this task will sleep for.
	 * 
	 * @param immediate
	 * 		The flag that denotes this task will execute immediately instead of being queued.
	 */
	public MobTask(T mob, int delay, boolean immediate) {		
		super(delay, immediate, StackType.STACK, BreakType.NEVER, TaskType.CURRENT_ACTION);
		this.mob = mob;		
	}
	
	/**
	 * Creates a new {@link MobTask}.
	 * 
	 * @param mob
	 * 		The mob that this task is for.
	 * 
	 * @param delay
	 * 		The delay in game ticks that this task will sleep for.
	 * 
	 * @param immediate
	 * 		The flag that denotes this task will execute immediately instead of being queued.
	 * 
	 * @param stackType
	 * 		The type of stack for this task.
	 * 
	 * @param breakType
	 * 		The type of break for this task.
	 * 
	 * @param taskType
	 * 		The type of task being executed.
	 */
	public MobTask(T mob, int delay, boolean immediate, StackType stackType, BreakType breakType, TaskType taskType) {
		super(delay, immediate, stackType, breakType, taskType);		
		this.mob = mob;		
	}

	/**
	 * Gets the mob.
	 */
	public T getMob() {
		return mob;
	}
	
}
