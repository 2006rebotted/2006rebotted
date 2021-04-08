package rs2.net.packet.out;

import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} that sends a hint arrow on an {@code entity}.
 * 
 * @author SeVen
 */
public final class SetMobHintIconPacket implements Sendable {

	/**
	 * The entity to display the arrow on.
	 */
	private final Mob entity;
	
	private final boolean reset;
	
	/**
	 * Creates a new {@link SetMobHintIconPacket}.
	 * 
	 * @param entity
	 * 		The entity to display the arrow on.
	 */
	public SetMobHintIconPacket(Mob entity) {
		this(entity, false);
	}

	/**
	 * Creates a new {@link SetMobHintIconPacket}.
	 * 
	 * @param entity
	 * 		The entity to display the arrow on.
	 */
	public SetMobHintIconPacket(Mob entity, boolean reset) {
		this.entity = entity;
		this.reset = reset;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(254);
		builder.write(entity.isPlayer() ? reset ? -1 : 10 : reset ? -1 : 1) // type
		.writeShort(entity.getSlot()) // slot
		.write(0) // x offset
		.write(0) // y offset
		.write(0); // z offset
		return builder.toOutgoingPacket();
	}

}
