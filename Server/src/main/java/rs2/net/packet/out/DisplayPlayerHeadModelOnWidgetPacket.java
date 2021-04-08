package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayPlayerHeadModelOnWidgetPacket implements Sendable {

	private final int interfaceId;

	public DisplayPlayerHeadModelOnWidgetPacket(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(185);
		builder.writeShort(interfaceId, ByteModification.ADDITION, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
