package astraeus.net.packet.in;

import astraeus.game.event.impl.MagicOnItemEvent;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.game.ByteBufReader;

/**
 * The {@link IncomingPacket} responsible for using magic on inventory items.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode(IncomingPacket.MAGIC_ON_ITEMS)
public final class MagicOnItemPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		final int slot = reader.readShort();
		final int itemId = reader.readShort(ByteModification.ADDITION);
		final int childId = reader.readShort();
		final int spellId = reader.readShort(ByteModification.ADDITION);
		
		player.post(new MagicOnItemEvent(itemId, slot, childId, spellId));
	}

}
