package astraeus.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import astraeus.game.sync.ClientSynchronizer;
import astraeus.util.LoggerUtils;

/**
 * Represents a simple engine that sequentially processes game logic.
 * 
 * @author Vult-R
 */
public abstract class GameService implements Runnable {

	/**
	 * The single logger for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerUtils.getLogger(GameService.class);

	/**
	 * The service that will manage the game.
	 */
	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());

	/**
	 * The synchronizer that will keeps in sync.
	 */
	protected final ClientSynchronizer synchronizer = new ClientSynchronizer(this);
	
	/**
	 * The future for this service.
	 */
	private ScheduledFuture<?> future;
	
	/**
	 * The rate in which the executor iterates the game loop.
	 */
	private static final int GAME_CYLCE_RATE = 600;
	
	/**
	 * The delay in milliseconds before running the game loop.
	 */
	private static final int GAME_DELAY = 600;

	/**
	 * Schedules the game service to run the main game loop.
	 */
	public void start() {
		setFuture(executor.scheduleAtFixedRate(new GameServiceSequencer(), GAME_DELAY, GAME_CYLCE_RATE, TimeUnit.MILLISECONDS));
	}

	/**
	 * Sets the current future for this game service.
	 * 
	 * @param future
	 * 		The scheduled future to set.
	 */
	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}
	
	/**
	 * Cancels this future.
	 * 
	 * @param interrupt
	 * 		The flag that denotes to interrupt the currently executing task.
	 */
	public void cancel(boolean interrupt) {
		future.cancel(interrupt);
	}
	
	@Override
	public void run() {
		runGameLoop();
	}
	
	/**
	 * The method for the game loop.
	 */
	abstract void runGameLoop();	

	/**
	 * Gets the scheduled executor for this service.
	 * 
	 * @return The scheduled executor.
	 */
	public ScheduledExecutorService getExecutor() {
		return executor;
	}

}
