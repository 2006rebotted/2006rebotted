package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;

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
