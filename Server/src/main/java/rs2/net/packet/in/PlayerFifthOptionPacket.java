package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PLAYER_OPTION_4)
public final class PlayerFifthOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
	
	}

}
