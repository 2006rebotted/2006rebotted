package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.PacketHeader;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetWidgetStringPacket implements Sendable {

	private final String string;

	private final int id;

	public SetWidgetStringPacket(String string, int id) {
		this.string = string;
		this.id = id;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(126, PacketHeader.VARIABLE_SHORT);
		builder.writeString(string)
		.writeShort(id, ByteModification.ADDITION);
		return builder.toOutgoingPacket();
	}
}
