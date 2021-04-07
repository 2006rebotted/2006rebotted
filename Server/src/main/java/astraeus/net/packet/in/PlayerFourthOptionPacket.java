package astraeus.net.packet.in;

import astraeus.game.model.World;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.PlayerRights;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.out.ServerMessagePacket;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PLAYER_OPTION_4)
public final class PlayerFourthOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		final int otherPlayerTradeIndex = reader.readShort(ByteOrder.LITTLE);

		if (otherPlayerTradeIndex == player.getSlot()) {
			return;
		}

		if (player.getRights().equal(PlayerRights.ADMINISTRATOR)) {
			player.queuePacket(new ServerMessagePacket("Trading as an admin has been disabled."));
			return;
		}

		if (otherPlayerTradeIndex < 1) {
			return;
		}

		if (World.world.getPlayers().get(otherPlayerTradeIndex) == null) {
			return;
		}

		Player other = (Player) World.world.getPlayers().get(otherPlayerTradeIndex);

		if (other == null || !other.isRegistered() || other.isTeleporting() || other.isDead()) {
			return;
		}
	}

}
