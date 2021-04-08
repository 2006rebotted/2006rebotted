package rs2.net.packet.out;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The packet responsible for removing ground items.
 *
 * @author Vult-R
 */
public final class RemoveGroundItemPacket implements Sendable {

	private final Item item;
	
    public RemoveGroundItemPacket(Item item) {
    	this.item = item;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
    	final GamePacketBuilder builder = new GamePacketBuilder(156);    	
    	builder.write(0, ByteModification.SUBTRACTION)
    	.writeShort(item.getId());
        return builder.toOutgoingPacket();
    }

}
