package rs2.net.packet.in;

import rs2.game.event.impl.ItemOnItemEvent;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

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

