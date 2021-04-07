package astraeus.game.sync;

import astraeus.game.model.World;
import astraeus.game.model.entity.mob.MobList;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.sync.task.PlayerUpdateTask;
import astraeus.game.sync.task.PostNpcUpdateTask;
import astraeus.game.sync.task.PostPlayerUpdateTask;
import astraeus.game.sync.task.PreNpcUpdateTask;
import astraeus.game.sync.task.PrePlayerUpdateTask;
import astraeus.service.GameService;

/**
 * The class that synchronizes player's clients with the server.
 * 
 * @author Vult-R
 */
public final class ClientSynchronizer {

	/**
	 * The service that runs the game.
	 */
	private final GameService service;

	/**
	 * Creates a new {@link ClientSynchronizer}.
	 * 
	 * @param service
	 *            The service that runs the game.
	 */
	public ClientSynchronizer(GameService service) {
		this.service = service;
	}

	/**
	 * Synchronizes the server with the client.
	 */
	public void synchronize() {
		
		MobList<Player> players = World.world.getPlayers();
		MobList<Npc> npcs = World.world.getMobs();
		
		players.forEach(player -> service.getExecutor().submit(new PrePlayerUpdateTask(player)));
		
		npcs.forEach(npc -> service.getExecutor().submit(new PreNpcUpdateTask(npc)));
		
		players.forEach(player -> service.getExecutor().submit(new PlayerUpdateTask(player)));		
		players.forEach(player -> service.getExecutor().submit(new PostPlayerUpdateTask(player)));
		
		npcs.forEach(npc -> service.getExecutor().submit(new PostNpcUpdateTask(npc)));
		
	}

}
