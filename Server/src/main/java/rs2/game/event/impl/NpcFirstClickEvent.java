package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.mob.npc.Npc;

public final class NpcFirstClickEvent implements Event {
	
	private final Npc npc;
	
	public NpcFirstClickEvent(Npc npc) {
		this.npc = npc;
	}

	public Npc getNpc() {
		return npc;
	}	

}
