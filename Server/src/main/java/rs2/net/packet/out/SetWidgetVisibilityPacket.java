package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} that shows an interface inside another interface.
 *
 * @author SeVen
 */
public final class SetWidgetVisibilityPacket implements Sendable {

    /**
     * The id of the interface to show.
     */
    private final int id;

    /**
     * The toggle to display the interface to the user.
     */
    private final boolean hide;

    /**
     * Creates a new {@link SetWidgetVisibilityPacket).
     *
     * @param id   The id of the interface.
     * @param hide The toggle to display the interface.
     */
    public SetWidgetVisibilityPacket(int id, boolean hide) {
        this.id = id;
        this.hide = hide;
    }

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(171);
        builder.write(hide ? 1 : 0)
                .writeShort(id);
        return builder.toOutgoingPacket();
    }

}
