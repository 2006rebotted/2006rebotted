package rs2.game.sync;

import rs2.game.model.World;
import rs2.game.model.entity.mob.MobList;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.sync.task.PlayerUpdateTask;
import rs2.game.sync.task.PostNpcUpdateTask;
import rs2.game.sync.task.PostPlayerUpdateTask;
import rs2.game.sync.task.PreNpcUpdateTask;
import rs2.game.sync.task.PrePlayerUpdateTask;
import rs2.service.GameService;

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
