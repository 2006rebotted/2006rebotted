package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayInventoryWidgetPacket implements Sendable {

	private final int widgetId;	

	private final int sidebarId;	

	public DisplayInventoryWidgetPacket(int widgetId, int sidebarId) {	
		
		this.widgetId = widgetId;
		this.sidebarId = sidebarId;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(248);
		builder.writeShort(widgetId, ByteModification.ADDITION)
		.writeShort(sidebarId);
		return builder.toOutgoingPacket();
	}

}
