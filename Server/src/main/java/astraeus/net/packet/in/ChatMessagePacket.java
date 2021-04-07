package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.ChatMessage;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

import java.util.logging.Logger;

/**
 * The {@link IncomingPacket} responsible for chat messages.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode(IncomingPacket.CHAT)
public class ChatMessagePacket implements Receivable {

	public static final Logger logger = Logger.getLogger(ChatMessagePacket.class.getName());

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int effects = reader.readByte(ByteModification.SUBTRACTION);		
		final int color = reader.readByte(ByteModification.SUBTRACTION);
		final int size = packet.getSize() - 2;

		final byte[] text = reader.readBytesReverse(size, ByteModification.ADDITION);

		if (effects < 0 || color < 0 || size < 0) {
			return;
		}

		player.setChatMessage(new ChatMessage(color, effects, text));
		player.getUpdateFlags().add(UpdateFlag.CHAT);
	}
}
