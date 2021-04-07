package rs2.game.event.impl;

import rs2.game.event.Event;

public final class WidgetContainerThirdOptionEvent implements Event {
	
	private final int widgetId;	
	
	private final int itemSlot;
	
	private final int itemId;
	
	public WidgetContainerThirdOptionEvent(int widgetId, int itemId, int itemSlot) {		
		this.widgetId = widgetId;
		this.itemId = itemId;
		this.itemSlot = itemSlot;
	}

	public int getWidgetId() {		
		return widgetId;
	}

	public int getItemSlot() {
		return itemSlot;
	}

	public int getItemId() {
		return itemId;
	}
	
}
