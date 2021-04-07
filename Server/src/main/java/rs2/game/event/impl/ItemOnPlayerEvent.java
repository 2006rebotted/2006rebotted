package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;

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
