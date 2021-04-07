package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.mob.npc.Npc;

public final class NpcSecondClickEvent implements Event {
	
	private final Npc npc;
	
	public NpcSecondClickEvent(Npc npc) {
		this.npc = npc;
	}

	public Npc getNpc() {
		return npc;
	}	

}
