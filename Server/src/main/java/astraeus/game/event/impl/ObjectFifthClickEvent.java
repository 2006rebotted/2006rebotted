package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.object.GameObject;

public final class ObjectFifthClickEvent implements Event {

	private final GameObject gameObject;	
	
	public ObjectFifthClickEvent(GameObject gameObject) {		
		this.gameObject = gameObject;
	}

	public GameObject getGameObject() {
		return gameObject;
	}
	
}
