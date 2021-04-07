package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.object.GameObject;

public final class DoorEvent implements Event {
	
	private GameObject door;
	
	public DoorEvent(GameObject door) {
		this.door = door;
	}

	public GameObject getDoor() {
		return door;
	}

}
