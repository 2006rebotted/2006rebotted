package astraeus.net.packet.in;

import astraeus.game.event.impl.ItemSecondClickEvent;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_OPTION_2)
public final class ItemSecondOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int itemId = reader.readShort(ByteModification.ADDITION);	

		player.post(new ItemSecondClickEvent(itemId, -1));
	}

}
