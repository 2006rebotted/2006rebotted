package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayWalkableWidgetPacket implements Sendable {

	private final int id;

	public DisplayWalkableWidgetPacket(int id) {
		this.id = id;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(208);
		builder.writeShort(id, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
