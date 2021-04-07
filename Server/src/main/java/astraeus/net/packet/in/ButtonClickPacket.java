package astraeus.net.packet.in;

import astraeus.game.event.impl.ButtonActionEvent;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.PlayerRights;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.out.ServerMessagePacket;
import astraeus.net.codec.game.ByteBufReader;

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