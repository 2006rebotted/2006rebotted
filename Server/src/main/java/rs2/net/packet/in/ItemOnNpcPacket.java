package rs2.net.packet.in;

import rs2.game.event.impl.ItemOnNpcEvent;
import rs2.game.model.World;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

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

