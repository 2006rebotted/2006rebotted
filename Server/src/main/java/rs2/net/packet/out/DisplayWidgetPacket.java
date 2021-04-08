package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayWidgetPacket implements Sendable {

	private final int interfaceId;

	public DisplayWidgetPacket(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(97);
		builder.writeShort(interfaceId);
		return builder.toOutgoingPacket();
	}

}
