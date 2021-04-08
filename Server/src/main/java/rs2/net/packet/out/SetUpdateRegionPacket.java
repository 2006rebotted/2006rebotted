package rs2.net.packet.out;

import rs2.game.model.Position;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetUpdateRegionPacket implements Sendable {

    private final Position position;

    public SetUpdateRegionPacket(Position position) {
        this.position = position;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(85);
        builder.write((position.getY() - 8 * player.getLastLocation().getRegionalY()), ByteModification.NEGATION)
                .write((position.getX() - 8 * player.getLastLocation().getRegionalX()), ByteModification.NEGATION);
        return builder.toOutgoingPacket();
    }

}
