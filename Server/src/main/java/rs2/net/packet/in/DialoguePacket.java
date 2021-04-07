package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;

/**
 * The {@link IncomingPacket} responsible for dialogues.
 * 
 * @author SeVen
 */
@IncomingPacketOpcode(40)
public final class DialoguePacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {

		player.getDialogueFactory().execute();

	}
}
