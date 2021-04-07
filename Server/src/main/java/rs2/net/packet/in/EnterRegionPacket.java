package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ENTER_REGION)
public final class EnterRegionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		
	}

}
