package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ENTER_REGION)
public final class EnterRegionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		
	}

}
