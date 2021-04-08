package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetSideBarWidgetPacket implements Sendable {

	private final int tabId;

	private final int interfaceId;

	public SetSideBarWidgetPacket(int tabId, int interfaceId) {
		this.tabId = tabId;
		this.interfaceId = interfaceId;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(71);
		builder.writeShort(interfaceId)
		.write(tabId, ByteModification.ADDITION);
		return builder.toOutgoingPacket();
	}

}
