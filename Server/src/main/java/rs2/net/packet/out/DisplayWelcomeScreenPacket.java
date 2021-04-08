package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayWelcomeScreenPacket implements Sendable {

    private final int days;

    private final int unreadMessages;

    private final int member;

    private final int ip;

    private final int daysSince;

    public DisplayWelcomeScreenPacket(int days, int unreadMessages, int member, int ip, int daysSince) {
        this.days = days;
        this.unreadMessages = unreadMessages;
        this.member = member;
        this.ip = ip;
        this.daysSince = daysSince;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(176);
        builder.write(days, ByteModification.NEGATION)
                .writeShort(unreadMessages, ByteModification.ADDITION)
                .write(member)
                .writeInt(ip, ByteOrder.MIDDLE)
                .writeShort(daysSince);

        return builder.toOutgoingPacket();
    }

}
