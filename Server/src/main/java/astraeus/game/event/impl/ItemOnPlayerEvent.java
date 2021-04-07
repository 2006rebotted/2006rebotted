package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.player.Player;

public final class ItemOnPlayerEvent implements Event {

	private final Item used;
	
	private final Player usedWith;
	
	public ItemOnPlayerEvent(Item used, Player usedWith) {
		this.used = used;
		this.usedWith = usedWith;
	}

	public Item getUsed() {
		return used;
	}

	public Player getUsedWith() {
		return usedWith;
	}
	
}
