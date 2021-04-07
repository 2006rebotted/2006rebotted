package rs2.net.packet.in;

import rs2.game.event.impl.CommandEvent;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;

/**
 * The {@link IncomingPacket} responsible for handling user commands send from the client.
 * 
 * @author Vult-R
 */
@IncomingPacketOpcode(IncomingPacket.PLAYER_COMMAND)
public final class CommandPacket implements Receivable {

      @Override
      public void handlePacket(Player player, IncomingPacket packet) {

			final String input = packet.getReader().getRS2String().trim().toLowerCase();
			
			player.post(new CommandEvent(input.split(" ")[0], input));
			
      }

}
