package rs2.net.packet.in;

import rs2.game.event.impl.ItemOnGroundItemEvent;
import rs2.game.model.Position;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.ServerMessagePacket;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_ON_GROUND_ITEM)
public final class ItemOnGroundItemPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		final int z = reader.readShort(ByteOrder.LITTLE);
		final int used = reader.readShort(false, ByteModification.ADDITION);		
		final int id = reader.readShort();		
		final int y = reader.readShort(false, ByteModification.ADDITION);
		final int slot = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);		
		final int x = reader.readShort();		

		if (player.getRights().equal(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
			player.queuePacket(new ServerMessagePacket("used: " + used + " slot: " + slot + " groundItem: " + id + " x: " + x + " y: " + y + " z: " + z));
		}
		
		final Item itemUsed = player.getInventory().get(slot);
		
		if (itemUsed.getId() != id) {
			return;
		}
		
		// grab this from a map of ground items, instead of creating the object like this.
		final Item groundItem = new Item(id);
		
		final Position position = new Position(x, y, z);
		
		player.post(new ItemOnGroundItemEvent(itemUsed, groundItem, position));	
		
	}

}
