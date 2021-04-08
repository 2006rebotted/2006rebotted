package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class AddFriendPacket implements Sendable {

    private final long username;

    private int world;

    public AddFriendPacket(long username, int world) {
        this.username = username;
        this.world = world;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(50);

        builder.writeLong(username);
        builder.write(world + 1);
        return builder.toOutgoingPacket();
    }

}
