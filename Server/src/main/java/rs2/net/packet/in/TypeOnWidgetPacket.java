package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;
import rs2.net.packet.Receivable;

@IncomingPacketOpcode({IncomingPacket.TYPE_ON_WIDGET})
public final class TypeOnWidgetPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		
	}

}
