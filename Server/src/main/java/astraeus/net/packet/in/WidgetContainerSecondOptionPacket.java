package astraeus.net.packet.in;

import astraeus.game.event.impl.WidgetContainerSecondOptionEvent;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.IncomingPacket.IncomingPacketOpcode;

@IncomingPacketOpcode(IncomingPacket.WIDGET_CONTAINER_OPTION_2)
public final class WidgetContainerSecondOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int widgetId = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		final int itemId = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		final int itemSlot = reader.readShort(ByteOrder.LITTLE);
		
		player.post(new WidgetContainerSecondOptionEvent(widgetId, itemId, itemSlot));	
	}

}
