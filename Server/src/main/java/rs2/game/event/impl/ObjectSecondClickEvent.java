package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.object.GameObject;

public final class ObjectSecondClickEvent implements Event {

	private final GameObject gameObject;	
	
	public ObjectSecondClickEvent(GameObject gameObject) {		
		this.gameObject = gameObject;
	}

	public GameObject getGameObject() {
		return gameObject;
	}
	
}