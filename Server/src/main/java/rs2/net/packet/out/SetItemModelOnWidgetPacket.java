package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetItemModelOnWidgetPacket implements Sendable {

    private final int id;

    private final int zoom;

    private final int model;

    public SetItemModelOnWidgetPacket(int id, int zoom, int model) {
        this.id = id;
        this.zoom = zoom;
        this.model = model;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(246);
        builder.writeShort(id, ByteOrder.LITTLE)
                .writeShort(zoom)
                .writeShort(model);
        return builder.toOutgoingPacket();
    }

}
