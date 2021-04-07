package rs2.game.event.impl;

import rs2.game.event.Event;
import rs2.game.model.entity.item.Item;

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
