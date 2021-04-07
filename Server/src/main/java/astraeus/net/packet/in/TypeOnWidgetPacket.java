package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.IncomingPacket.IncomingPacketOpcode;
import astraeus.net.packet.Receivable;

@IncomingPacketOpcode({IncomingPacket.TYPE_ON_WIDGET})
public final class TypeOnWidgetPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		
	}

}
