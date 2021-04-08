package rs2.net.packet.out;

import java.util.Optional;

import rs2.game.model.entity.mob.combat.dmg.Poison.PoisonType;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

public final class SetPoisonPacket implements Sendable {
	
	/**
	 * The type of poison.
	 */
	private final PoisonType type;	
	
	/**
	 * The type of poison.
	 */
	public SetPoisonPacket(PoisonType type) {
		this.type = type;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder();
		builder.write(type.getType(), ByteModification.NEGATION);
		return builder.toOutgoingPacket();
	}

}
