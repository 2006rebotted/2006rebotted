package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetPlayerSlotPacket implements Sendable {

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {

		GamePacketBuilder builder = new GamePacketBuilder(249);

		builder.write(1, ByteModification.ADDITION)
		.writeShort(player.getSlot(), ByteModification.ADDITION, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
