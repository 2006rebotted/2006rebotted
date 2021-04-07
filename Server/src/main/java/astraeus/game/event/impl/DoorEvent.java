package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.object.GameObject;

public final class DoorEvent implements Event {
	
	private GameObject door;
	
	public DoorEvent(GameObject door) {
		this.door = door;
	}

	public GameObject getDoor() {
		return door;
	}

}
