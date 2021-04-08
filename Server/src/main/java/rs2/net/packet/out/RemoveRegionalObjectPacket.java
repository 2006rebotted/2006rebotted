package rs2.net.packet.out;

import rs2.game.model.Direction;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.object.GameObject;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} implementation that removes an object from a users client.
 *
 * @author Seven
 */
public final class RemoveRegionalObjectPacket implements Sendable {

	/**
	 * The {@code object} that is being removed.
	 */
	private final GameObject object;

	private final boolean normal;

	/**
	 * Creates a new {@link RemoveRegionalObjectPacket} packet.
	 *
	 * @param object The object to remove.
	 */
	public RemoveRegionalObjectPacket(GameObject object, boolean normal) {
		this.object = object;
		this.normal = normal;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		player.queuePacket(new SetUpdateRegionPacket(object.getPosition()));
		GamePacketBuilder builder = new GamePacketBuilder(101);
		builder.write(object.getType() << 2 | (normal ? object.getOrientation() : Direction.getDoorOrientation(object.getEnumeratedOrientation()) & 3), ByteModification.NEGATION)
				.write(0);
		return builder.toOutgoingPacket();
	}

}
