package rs2.net.packet.in;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.game.model.entity.object.GameObjects;
import rs2.net.codec.ByteModification;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.out.AddGroundItemPacket;
import rs2.net.packet.out.ServerMessagePacket;

/**
 * The {@link IncomingPacket} responsible for dropping items.
 * 
 * @author SeVen
 */
@IncomingPacketOpcode(IncomingPacket.DROP_ITEM)
public final class DropItemPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {	
		ByteBufReader reader = packet.getReader();
		
		final int itemId = reader.readShort(false, ByteModification.ADDITION);
		
		reader.readByte(false);
		reader.readByte(false);

        final int slot = reader.readShort(false, ByteModification.ADDITION);
        
        final Item item = player.getInventory().get(slot);
        
        if (item == null) {
        	return;
        }       

        //TODO add destoryable items
		boolean droppable = true;

		if (!droppable) {
			player.queuePacket(new ServerMessagePacket("This item cannot be dropped."));
			return;
		}

		if (player.getRights().equals(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
			player.queuePacket(new ServerMessagePacket("ItemDropped: " + itemId));
		}
		
		player.queuePacket(new AddGroundItemPacket(item));
		
		GameObjects.getGroundItems().put(player.getPosition().copy(), item);
		
		player.getInventory().remove(item);	
		
	}

}
