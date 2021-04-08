package rs2.net.packet.out;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link Sendable} implementation that displays an item on the ground.
 * 
 * @author Vult-R
 */
public final class AddGroundItemPacket implements Sendable {

	private Item item;

	public AddGroundItemPacket(Item item) {
		this.item = item;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		player.queuePacket(new SetUpdateRegionPacket(player.getPosition()));
		
		final GamePacketBuilder builder = new GamePacketBuilder(44);
		builder.writeShort(item.getId(), ByteModification.ADDITION, ByteOrder.LITTLE)
		.writeShort(item.getAmount())
		.write(0); // offset
		return builder.toOutgoingPacket();
	}

}
