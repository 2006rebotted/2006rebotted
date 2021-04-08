package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayLocationHintIconOutgoingPacket implements Sendable {

	/**
	 * The x coordinate for this location.
	 */
	private final int x;

	/**
	 * The y coordinate for this location.
	 */
	private final int y;

	/**
	 * The height for this location.
	 */
	private final int height;

	/**
	 * The direction to face.
	 */
	private final int direction;

	/**
	 * Creates a new {@link DisplayLocationHintIconOutgoingPacket}.
	 * 
	 * @param x
	 *            The x coordinate for this icon.
	 * 
	 * @param y
	 *            The y coordinate for this icon.
	 * 
	 * @param height
	 *            The height for this icon.
	 * 
	 * @param direction
	 *            The facing direction of the arrow.
	 * 
	 */
	public DisplayLocationHintIconOutgoingPacket(int x, int y, int height, int direction) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.direction = direction;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(254);
		builder.write(direction)
		.writeShort(x)
		.writeShort(y)
		.write(height);
		return builder.toOutgoingPacket();
	}

}
