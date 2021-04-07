package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.npc.Npc;

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
