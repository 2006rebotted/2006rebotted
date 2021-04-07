package rs2.service;

import rs2.game.model.World;

/**
 * The {@link GameService} sequencer implementation.
 * 
 * @author Vult-R
 */
public final class GameServiceSequencer extends GameService {

	@Override
	public void runGameLoop() {
		
		World.world.dequeueLogin();
		
		World.world.getTasks().process();
		
		synchronizer.synchronize();
		
		World.world.dequeueLogout();

	}

}
