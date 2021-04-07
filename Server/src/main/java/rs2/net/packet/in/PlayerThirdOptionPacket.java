package rs2.net.packet.in;

import rs2.game.model.World;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PLAYER_OPTION_3)
public final class PlayerThirdOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int otherPlayerIndex = reader.readShort(ByteOrder.LITTLE);

		if (World.world.getPlayers().get(otherPlayerIndex) == null) {
			return;
		}

		@SuppressWarnings("unused")
            final Player leader = (Player) World.world.getPlayers().get(otherPlayerIndex);
	}

}
