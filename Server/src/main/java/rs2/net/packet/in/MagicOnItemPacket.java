package rs2.net.packet.in;

import rs2.game.event.impl.MagicOnItemEvent;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.ByteBufReader;

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
