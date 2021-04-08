package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class UpdateSkillPacket implements Sendable {

	private final int id;

	private final int level;

	private final int experience;

	public UpdateSkillPacket(int id, int level, int experience) {
		this.id = id;
		this.level = level;
		this.experience = experience;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
			GamePacketBuilder builder = new GamePacketBuilder(134);
			builder.write(id);
			builder.writeInt(experience, ByteOrder.MIDDLE);
			builder.write(level);
			return builder.toOutgoingPacket();
	}

}
