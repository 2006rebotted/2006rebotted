package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.object.GameObject;

public final class ObjectFourthClickEvent implements Event {

	private final GameObject gameObject;	
	
	public ObjectFourthClickEvent(GameObject gameObject) {		
		this.gameObject = gameObject;
	}

	public GameObject getGameObject() {
		return gameObject;
	}
	
}
