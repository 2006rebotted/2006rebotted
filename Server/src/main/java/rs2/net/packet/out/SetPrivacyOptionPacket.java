package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetPrivacyOptionPacket implements Sendable {

    private final int publicChat;

    private final int privateChat;

    private final int tradeChat;

    public SetPrivacyOptionPacket(int publicChat, int privateChat, int tradeChat) {
        this.publicChat = publicChat;
        this.privateChat = privateChat;
        this.tradeChat = tradeChat;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(206);

        builder.write(publicChat)
                .write(privateChat)
                .write(tradeChat);

        return builder.toOutgoingPacket();
    }

}
