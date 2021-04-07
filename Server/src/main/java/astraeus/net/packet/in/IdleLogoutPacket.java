package astraeus.net.packet.in;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.IncomingPacket.IncomingPacketOpcode;

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