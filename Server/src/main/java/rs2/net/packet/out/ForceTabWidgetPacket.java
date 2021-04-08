package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class ForceTabWidgetPacket implements Sendable {

	private final int id;

	public ForceTabWidgetPacket(int id) {
		this.id = id;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(106);
		builder.write(id, ByteModification.NEGATION);
		player.getUpdateFlags().add(UpdateFlag.APPEARANCE);
		return builder.toOutgoingPacket();
	}

}
