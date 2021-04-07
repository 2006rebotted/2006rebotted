package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

/**
 * The {@link IncomingPacket} responsible for various clicking in-game.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode({IncomingPacket.CAMERA_MOVEMENT, IncomingPacket.IDLE_LOGOUT, IncomingPacket.FOCUS_CHANGE})
public class DefaultPacket implements Receivable {

    @Override
    public void handlePacket(Player player, IncomingPacket packet) {

    }

}
