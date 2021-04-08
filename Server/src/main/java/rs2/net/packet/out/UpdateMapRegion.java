package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class UpdateMapRegion implements Sendable {

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(73);
		player.setLastPosition(player.getPosition().copy());
		builder.writeShort(player.getPosition().getRegionalX() + 6, ByteModification.ADDITION)
		.writeShort(player.getPosition().getRegionalY() + 6);
		return builder.toOutgoingPacket();
	}

}
