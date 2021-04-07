package astraeus.game.model.entity.mob.player.event;

import astraeus.game.event.Event;
import astraeus.game.model.entity.mob.player.Player;

public final class PostLoginEvent implements Event {
	
	private final Player player;
	
	public PostLoginEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

}
