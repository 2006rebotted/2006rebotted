package astraeus.net.packet.in;

import astraeus.game.event.impl.ItemOnPlayerEvent;
import astraeus.game.model.World;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteOrder;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_ON_PLAYER)
public final class ItemOnPlayerPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final int playerIndex = packet.getReader().readShort(false);
		final int itemSlot = packet.getReader().readShort(ByteOrder.LITTLE);
		
		final Item used = player.getInventory().get(itemSlot);
		
		final Player usedWith = World.world.getPlayers().get(playerIndex);
		
		player.post(new ItemOnPlayerEvent(used, usedWith));	

	}

}
