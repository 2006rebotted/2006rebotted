package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.IncomingPacket.IncomingPacketOpcode;

@IncomingPacketOpcode(IncomingPacket.WIDGET_CONTAINER_OPTION_5)
public final class WidgetContainerFifthOptionPacket implements Receivable {

	@SuppressWarnings("unused")
	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		int itemSlot = reader.readShort(ByteOrder.LITTLE);
		int interfaceId = reader.readShort(ByteModification.ADDITION);		
		int itemId = reader.readShort(ByteOrder.LITTLE);		
	}

}
