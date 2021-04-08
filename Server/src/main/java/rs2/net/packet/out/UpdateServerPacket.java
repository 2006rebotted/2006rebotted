package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} that sends a system update.
 * 
 * @author SeVen
 */
public final class UpdateServerPacket implements Sendable {

	/**
	 * The amount of seconds.
	 */
	private int seconds;

	/**
	 * Creates a new {@link UpdateServerPacket}.
	 * 
	 * @param seconds
	 *            The amount of seconds before a system update occurs.
	 */
	public UpdateServerPacket(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(114);
		builder.writeShort(seconds * 50 / 30, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
