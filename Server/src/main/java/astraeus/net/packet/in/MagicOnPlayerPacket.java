package astraeus.net.packet.in;

import astraeus.game.model.World;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.out.ServerMessagePacket;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.MAGIC_ON_PLAYER)
public final class MagicOnPlayerPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		int otherPlayerIndex = reader.readShort(ByteModification.ADDITION);
		Player other = (Player) World.world.getPlayers().get(otherPlayerIndex);
		@SuppressWarnings("unused")
		final int spell = reader.readShort(ByteOrder.LITTLE);

		if (other == null) {
			player.queuePacket(new ServerMessagePacket("You tried to attack a player that doesn't exist."));
			return;
		}
	}

}
