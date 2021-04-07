package astraeus.net.packet.in;

import astraeus.game.event.impl.ItemThirdClickEvent;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_OPTION_3)
public final class ItemThirdOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int itemId11 = reader.readShort(ByteOrder.LITTLE);
		final int itemId1 = reader.readShort(ByteModification.ADDITION);	
		final int itemId = reader.readShort(ByteModification.ADDITION);	

		player.post(new ItemThirdClickEvent(itemId, itemId11, itemId1));
	}

}
