package rs2.net.packet.in;

import rs2.game.event.impl.ItemThirdClickEvent;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_OPTION_3)
public final class ItemThirdOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int itemId11 = reader.readShort(ByteOrder.LITTLE);
		final int itemId1 = reader.readShort(ByteModification.ADDITION);	
		final int itemId = reader.readShort(ByteModification.ADDITION);	

		player.post(new ItemThirdClickEvent(itemId, itemId11, itemId1));
	}

}
