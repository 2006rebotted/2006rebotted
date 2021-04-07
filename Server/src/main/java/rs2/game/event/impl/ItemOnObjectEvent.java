package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.object.GameObject;

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
