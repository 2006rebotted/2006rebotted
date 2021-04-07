package astraeus.net.packet.in;

import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.PlayerRights;
import astraeus.game.model.entity.object.GameObjects;
import astraeus.net.codec.ByteModification;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.IncomingPacket.IncomingPacketOpcode;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.out.AddGroundItemPacket;
import astraeus.net.packet.out.ServerMessagePacket;

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
