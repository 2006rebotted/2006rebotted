package rs2.game.model.entity.mob.player.event;

import rs2.game.event.Event;
import rs2.game.model.entity.mob.player.Player;

public final class LogoutEvent implements Event {
	
	private final Player player;
	
	public LogoutEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
	

}
