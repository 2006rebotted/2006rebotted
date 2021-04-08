package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayMultiIconPacket implements Sendable {

	private final boolean hide;

	public DisplayMultiIconPacket(boolean hide) {
		this.hide = hide;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(61);
		builder.write(hide ? 0 : 1);
		return builder.toOutgoingPacket();
	}

}
