package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.IncomingPacket.IncomingPacketOpcode;

/**
 * The {@link IncomingPacket} responsible logging out a player after a certain
 * amount of time.
 * 
 * @author SeVen
 */
@IncomingPacketOpcode(IncomingPacket.IDLE_LOGOUT)
public final class IdleLogoutPacket implements Receivable {

    @Override
    public void handlePacket(Player player, IncomingPacket packet) {

    }
}