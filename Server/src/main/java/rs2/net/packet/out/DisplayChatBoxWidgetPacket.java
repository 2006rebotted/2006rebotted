package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayChatBoxWidgetPacket implements Sendable {

	private final int interfaceId;

	public DisplayChatBoxWidgetPacket(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(164);
		builder.writeShort(interfaceId, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
