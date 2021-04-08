package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetSpecialAmountPacket implements Sendable {

    @Override
    public Optional<OutgoingPacket> writePacket(Player player) {
        GamePacketBuilder builder = new GamePacketBuilder(137);

		if (player.getSpecialAmount() > 100) {
			player.setSpecialAmount(100);
		}
		
		if (player.getSpecialAmount() < 0) {
			player.setSpecialAmount(0);
		}
        
        builder.write(player.getSpecialAmount());
        return builder.toOutgoingPacket();
    }
}
