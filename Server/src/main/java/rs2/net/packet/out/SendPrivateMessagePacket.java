package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.PacketHeader;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SendPrivateMessagePacket implements Sendable {

    private final long name;

    private final PlayerRights rights;

    private final byte[] message;

    private final int size;

    public SendPrivateMessagePacket(long name, PlayerRights rights, byte[] message, int size) {
        this.name = name;
        this.rights = rights;
        this.message = message;
        this.size = size;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(196, PacketHeader.VARIABLE_BYTE);
        builder.writeLong(name)
                .writeInt(player.lastMessage++)
                .write(rights.getProtocolValue())
                .writeBytes(message, size);
        return builder.toOutgoingPacket();
    }

}
