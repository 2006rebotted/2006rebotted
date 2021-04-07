package rs2.net.packet.in;

import rs2.game.event.impl.ItemOnPlayerEvent;
import rs2.game.model.World;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

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
