package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PLAYER_OPTION_4)
public final class PlayerFifthOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
	
	}

}
