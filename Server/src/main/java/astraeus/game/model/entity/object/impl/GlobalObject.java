package astraeus.game.model.entity.object.impl;

import astraeus.game.model.Direction;
import astraeus.game.model.Position;

public final class GlobalObject {
	
	private final int id;
	
	private final String name;
	
	private final int type;
	
	private final Position location;
	
	private final Direction orientation;

	public GlobalObject(int id, String name, int type, Position location, Direction orientation) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.location = location;
		this.orientation = orientation;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public Position getLocation() {
		return location;
	}

	public Direction getOrientation() {
		return orientation;
	}
	
}
