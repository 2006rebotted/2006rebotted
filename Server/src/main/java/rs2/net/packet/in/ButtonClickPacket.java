package rs2.net.packet.in;

import rs2.game.event.impl.ButtonActionEvent;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.ServerMessagePacket;
import rs2.net.codec.game.ByteBufReader;

/**
 * The {@link IncomingPacket} responsible for clicking buttons on the client.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode(IncomingPacket.BUTTON_CLICK)
public class ButtonClickPacket implements Receivable {

	@Override
	public void handlePacket(final Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int button = reader.readShort();
		
		if (player.getRights().greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
			player.queuePacket(new ServerMessagePacket(String.format("[button= %d]", button)));
		}		
		
		player.post(new ButtonActionEvent(button));
	}

}