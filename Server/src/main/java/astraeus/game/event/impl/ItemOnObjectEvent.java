package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.object.GameObject;

public final class ItemOnObjectEvent implements Event {
	
	private final Item item;
	
	private final GameObject gameObject;
	
	public ItemOnObjectEvent(Item item, GameObject gameObject) {
		this.item = item;
		this.gameObject = gameObject;
	}

	public Item getItem() {
		return item;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

}
