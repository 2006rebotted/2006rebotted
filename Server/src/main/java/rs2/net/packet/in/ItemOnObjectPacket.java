package rs2.net.packet.in;

import rs2.game.event.impl.ItemOnObjectEvent;
import rs2.game.model.Position;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.object.GameObject;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_ON_OBJECT)
public final class ItemOnObjectPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		@SuppressWarnings("unused")
		int interfaceType = reader.readShort();
		final int objectId = reader.readShort(ByteOrder.LITTLE);
		final int objectY = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		final int slot = reader.readShort(ByteOrder.LITTLE);
		final int objectX = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		final int itemId = reader.readShort();
		
		final Item item = player.getInventory().get(slot);
		
		// validate the item exists and is the correct item
		if (item.getId() != itemId) {
			return;
		}

		// instead of doing it this way, when clipping gets added grab the game object from a map of objects
		GameObject object = new GameObject(objectId, new Position(objectX, objectY));

		player.post(new ItemOnObjectEvent(item, object));
	}

}

