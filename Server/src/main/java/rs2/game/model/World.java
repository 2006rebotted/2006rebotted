package rs2.game.model;

import rs2.game.GameConstants;
import rs2.game.event.Event;
import rs2.game.event.EventSubscriber;
import rs2.game.event.UniversalEventProvider;
import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.MobList;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.event.RegisterPlayerEvent;
import rs2.game.task.Task;
import rs2.game.task.TaskManager;
import rs2.plugin.PluginService;
import rs2.util.LoggerUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Represents the game world.
 * 
 * @author SeVen
 */
public final class World {

	/**
	 * The single logger for this class.
	 */
	public final Logger logger = LoggerUtils.getLogger(World.class);

	/**
	 * The collection of players in the game world.
	 */
	private final MobList<Player> players = new MobList<Player>(GameConstants.MAX_PLAYERS);	
	
	/**
	 * The collection of npcs in the game world.
	 */
	private final MobList<Npc> npcs = new MobList<Npc>(GameConstants.MAX_NPC_SPAWNS);

	/**
	 * The {@link Set} of banned hosts.
	 */
	private final Set<String> ipBans = new HashSet<>();	

	/**
	 * The {@link Set} of banned mac addresses.
	 */
	private final Set<String> bannedUUIDs = new HashSet<>();

	/**
	 * The {@link Player}s waiting to login.
	 */
	private final Queue<Player> logins = new ConcurrentLinkedQueue<>();

	/**
	 * The {@link Player}s waiting to logout.
	 */
	private final Queue<Player> logouts = new ConcurrentLinkedQueue<>();

	/**
	 * This worlds event provider.
	 */
	private final UniversalEventProvider eventProvider = new UniversalEventProvider();
	
	/**
	 * The service for plugins.
	 */
	private final PluginService pluginService = new PluginService();	
	
	/**
	 * The tasks for this world.
	 */
    private final TaskManager tasks = new TaskManager();
    
    /**
     * The single instance of world
     */
    //TODO scrap this singleton
    public static final World world = new World();

	/**
	 * Registers and adds a {@code entity) into the game world.
	 * 
	 * @param entity
	 *            The entity to add.
	 */
	public void register(Mob entity) {
		// check to make this entity is not registered already, and is present.
		if (entity == null || entity.isRegistered()) {
			return;
		}

		if (entity.isPlayer()) {
			Player player = (Player) entity;
			player.setId(-1);
			players.add(player);
		}
	}

	/**
	 * Deregisters a {@link Mob} from the game world.
	 * 
	 * @param entity
	 *            The entity to remove.
	 */
	public void deregister(Mob entity) {

		if (entity == null || !entity.isRegistered()) {
			return;
		}

		if (entity.isPlayer()) {
			players.remove(entity);
		} else {
			npcs.remove(entity);
		}
	}

	/**
	 * The function that makes a player wait until they can be added into the game.
	 * 
	 * @param player
	 */
	public void queueLogin(Player player) {
		if (player.getSession() != null && !logins.contains(player)) {
			logins.add(player);
		}
	}
	
	/**
	 * The function that allows players to login.
	 */
	public void dequeueLogin() {
		for (int index = 0; index < GameConstants.LOGIN_LIMIT; index++) {
			Player player = logins.poll();

			if (player == null) {
				break;
			}

			post(player, new RegisterPlayerEvent(player));
		}
	}

	/**
	 * The function that makes a player wait until they can be logged out in sync with the server.
	 * 
	 * @param player
	 */
	public void queueLogout(Player player) {
		if (player != null && !logouts.contains(player)) {
			logouts.add(player);
		}
	}
	
	/**
	 * The function that logs out players from the game world.
	 */
	public void dequeueLogout() {
		for(int index = 0; index < logouts.size(); index++) {
			Player player = logouts.poll();
			
			if (player == null || index >= GameConstants.LOGOUT_LIMIT) {
				break;
			}
			
			player.onDeregister();
		}
	}

	/**
	 * Searches the collection of players and retrieves the player with the specified name
	 * 
	 * @param name
	 */
	public Optional<Player> searchPlayer(String name) {
		return players.stream().filter(Objects::nonNull).filter(it -> name.equalsIgnoreCase(it.getUsername())).findFirst();
	}

	/**
	 * Posts an event to this worlds event provider.
	 *
	 * @param player
	 *            The player to post the event for.
	 * @param event
	 *            The event to post.
	 */
	public <E extends Event> void post(Player player, E event) {
		eventProvider.post(player, event);
	}

	/**
	 * Provides an event subscriber to this worlds event provider.
	 *
	 * @param subscriber
	 *            The event subscriber.
	 */
	public <E extends Event> void provideSubscriber(EventSubscriber<E> subscriber) {
		eventProvider.provideSubscriber(subscriber);
	}

	/**
	 * Deprives an event subscriber to this worlds event provider.
	 *
	 * @param subscriber
	 *            The event subscriber.
	 */
	public <E extends Event> void depriveSubscriber(EventSubscriber<E> subscriber) {
		eventProvider.depriveSubscriber(subscriber);
	}

	/**
	 * Submits a new {@link Task}.
	 * 
	 * @param task
	 *            The task to execute.
	 */
	public void submit(Task task) {
		tasks.queue(task);		
	}

	/**
	 * Gets the collection the players in this world.
	 * 
	 * @return The collection of players.
	 */
	public MobList<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the collection of mobs in this world.
	 * 
	 * @return The collection of mobs.
	 */
	public MobList<Npc> getMobs() {
		return npcs;
	}

	/**
	 * Gets the players logging in.
	 * 
	 * @return The queue containing players logging in.
	 */
	public Queue<Player> getLogins() {		
		return logins;
	}

	/**
	 * Gets the players logging out.
	 * 
	 * @return The queue containing players logging out.
	 */
	public Queue<Player> getLogouts() {		
		return logouts;
	}

	/**
	 * Gets the set of banned hosts.
	 *
	 * @return The set of banned hosts.
	 */
	public Set<String> getIpBans() {
		return ipBans;
	}

	/**
	 * Gets the set of banned mac addresses.
	 *
	 * @return The set of banned mac addresses.
	 */
	public Set<String> getBannedUUIDs() {			
		return bannedUUIDs;		
	}
	
	public TaskManager getTasks() {
		return tasks;
	}
	
	/**
	 * Gets the service for plugins.
	 */
	public PluginService getPluginService() {		
		return pluginService;
	}
	
	public UniversalEventProvider getSubscribers() {
		return eventProvider;
	}

}
