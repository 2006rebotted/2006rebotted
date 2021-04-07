package astraeus.net.packet.in;

import astraeus.game.event.impl.ItemOnNpcEvent;
import astraeus.game.model.World;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_ON_NPC)
public final class ItemOnNpcPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int itemId = reader.readShort(false, ByteModification.ADDITION);
		final int npcSlot = reader.readShort(false, ByteModification.ADDITION);
		final int itemSlot = reader.readShort(ByteOrder.LITTLE);
		
		final Item item = player.getInventory().get(itemSlot);
		
		// validate the item is the correct item
		if (item.getId() != itemId) {
			return;
		}

		final Npc npc = World.world.getMobs().get(npcSlot);

		// validate the npc actually exists
		if (npc == null) {
			return;
		}
		
		player.post(new ItemOnNpcEvent(item, npc));
	}

}

