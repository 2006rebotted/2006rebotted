package rs2.net.packet.in;

import rs2.game.event.impl.WidgetContainerThirdOptionEvent;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;

@IncomingPacketOpcode(IncomingPacket.WIDGET_CONTAINER_OPTION_3)
public final class WidgetContainerThirdOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int widgetId = reader.readShort(ByteOrder.LITTLE);		
		final int itemId = reader.readShort(ByteModification.ADDITION);
		final int itemSlot = reader.readShort(ByteModification.ADDITION);
		
		player.post(new WidgetContainerThirdOptionEvent(widgetId, itemId, itemSlot));
	}

}
