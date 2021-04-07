package astraeus.game.task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import astraeus.game.task.Task.StackType;

/**
 * The class that will manage all tasks.
 * 
 * @author Vult-R
 */
public final class TaskManager {

	/**
	 * The queue that holds the tasks awaiting to be executed.
	 */
	private final Queue<Task> adding = new ConcurrentLinkedQueue<Task>();
	
	/**
	 * The list of tasks that are being processed.
	 */
	private final List<Task> tasks = new LinkedList<Task>();

	/**
	 * The method that will process all tasks.
	 */
	public void process() {
		Task t;

		synchronized (adding) {
			while ((t = adding.poll()) != null) {
				tasks.add(t);
			}
		}

		for (final Iterator<Task> i = tasks.iterator(); i.hasNext();) {
			final Task task = i.next();
			try {
				if (task.hasStopped()) {
					task.onStop();
					i.remove();
					continue;
				}

				task.run();
			} catch (final Exception e) {
				e.printStackTrace();
				i.remove();
			}
		}
	}

	/**
	 * Queues a task to be sequenced with the main game loop.
	 * 
	 * @param task
	 * 		The task to queue.
	 */
	public void queue(Task task) {
		if (task.hasStopped()) {
			return;
		}

		if (task.getStackType() == StackType.NEVER_STACK) {
			for (final Iterator<Task> i = tasks.iterator(); i.hasNext();) {
				final Task t = i.next();

				if (t.getStackType() == StackType.NEVER_STACK && t.getTaskType() == task.getTaskType()) {
					return;
				}
			}
		}

		task.onStart();

		if (task.isImmediate()) {
			task.execute();
		}

		adding.add(task);
	}

}
