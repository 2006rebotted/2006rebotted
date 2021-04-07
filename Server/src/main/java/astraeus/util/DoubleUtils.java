package astraeus.util;

import astraeus.game.model.Position;

public class DoubleUtils {
	
	public static double distance(Position first, Position second) {
		final int dx = second.getX() - first.getX();
		final int dy = second.getY() - first.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

}
