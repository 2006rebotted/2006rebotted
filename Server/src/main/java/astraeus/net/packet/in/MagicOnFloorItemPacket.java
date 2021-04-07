package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;

/**
 * The {@link IncomingPacket} responsible for using magic on ground items.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode(181)
public class MagicOnFloorItemPacket implements Receivable {

	@SuppressWarnings("unused")
	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		final int itemY = reader.readShort(ByteOrder.LITTLE);
		final int itemId = reader.readShort(false);
		final int itemX = reader.readShort(ByteOrder.LITTLE);
		final int spellId = reader.readShort(false, ByteModification.ADDITION);

	}

}
