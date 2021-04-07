package rs2.net.packet.in;

import rs2.game.event.impl.ItemSecondClickEvent;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_OPTION_2)
public final class ItemSecondOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int itemId = reader.readShort(ByteModification.ADDITION);	

		player.post(new ItemSecondClickEvent(itemId, -1));
	}

}
