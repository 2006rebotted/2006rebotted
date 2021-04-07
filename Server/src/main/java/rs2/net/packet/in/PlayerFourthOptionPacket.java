package rs2.net.packet.in;

import rs2.game.model.World;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.ServerMessagePacket;

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
