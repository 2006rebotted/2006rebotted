package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.PacketHeader;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} that sends a message to a {@link Player}s chatbox.
 * 
 * @author SeVen
 */
public final class ServerMessagePacket implements Sendable {

	/**
	 * The message to send.
	 */
	private final String message;

	/**
	 * Creates a new {@link ServerMessagePacket}.
	 */
	public ServerMessagePacket(String message) {
		this.message = message;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(253, PacketHeader.VARIABLE_BYTE);
		builder.writeString(message);
		return builder.toOutgoingPacket();
	}

}
