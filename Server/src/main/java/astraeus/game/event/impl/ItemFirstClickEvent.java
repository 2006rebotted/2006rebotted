package astraeus.game.event.impl;

import astraeus.game.event.Event;
import astraeus.game.model.entity.item.Item;

public final class ItemFirstClickEvent implements Event {
	
	private final Item item;
	
	private final int widgetId;
	
	public ItemFirstClickEvent(Item item, int widgetId) {
		this.item = item;
		this.widgetId = widgetId;
	}

	public Item getItem() {
		return item;
	}
	
	public int getWidgetId() {
		return widgetId;
	}	

}
