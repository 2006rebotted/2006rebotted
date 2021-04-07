package astraeus.net.packet.in;

import astraeus.game.event.impl.ItemOnItemEvent;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_ON_ITEM)
public final class ItemOnItemPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int usedWithSlot = reader.readShort();
		final int itemUsedSlot = reader.readShort(ByteModification.ADDITION);
		
		final Item used = player.getInventory().get(itemUsedSlot);
		
		final Item with = player.getInventory().get(usedWithSlot);		

		player.post(new ItemOnItemEvent(used, with));
	}

}

