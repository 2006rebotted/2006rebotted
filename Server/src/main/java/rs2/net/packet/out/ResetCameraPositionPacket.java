package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class ResetCameraPositionPacket implements Sendable {

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
	    return new GamePacketBuilder(107).toOutgoingPacket();
    }

}
