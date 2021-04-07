package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;

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
