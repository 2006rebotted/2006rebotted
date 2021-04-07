package astraeus.net.packet;

import astraeus.game.model.entity.mob.player.Player;

/**
 * The interface that allows any implementing {@Packet}s. The ability to be
 * intercepted as an incoming packets.
 * 
 * @author Vult-R
 */
@FunctionalInterface
public interface Receivable {

    /**
     * Handles the packet that has just been received.
     * 
     * @param player
     *            The player receiving this packet.
     * 
     * @param packet
     *            The packet that has been received.
     */
    public void handlePacket(Player player, IncomingPacket packet);
}
