package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.npc.Npc;

public final class ItemOnNpcEvent implements Event {
	
	private final Item item;
	
	private final Npc npc;
	
	public ItemOnNpcEvent(Item item, Npc npc) {
		this.item = item;
		this.npc = npc;
	}

	public Item getItem() {
		return item;
	}

	public Npc getNpc() {
		return npc;
	}

}
