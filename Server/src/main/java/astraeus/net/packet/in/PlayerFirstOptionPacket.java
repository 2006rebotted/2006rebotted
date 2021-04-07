package astraeus.net.packet.in;

import astraeus.game.model.World;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PLAYER_OPTION_1)
public final class PlayerFirstOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int otherPlayerIndex = reader.readShort(ByteOrder.LITTLE);
		
		if (World.world.getPlayers().get(otherPlayerIndex) == null) {
			return;
		}
	}

}
