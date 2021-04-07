package rs2.net.packet.in;

import rs2.game.model.World;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.ServerMessagePacket;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PLAYER_OPTION_2)
public final class PlayerSecondOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		int otherPlayerIndex = reader.readShort(ByteOrder.LITTLE);
		Player other = (Player) World.world.getPlayers().get(otherPlayerIndex);

		if (other == null) {
			player.queuePacket(new ServerMessagePacket("You tried to attack a player that doesn't exist."));
			return;
		}		
	}

}
