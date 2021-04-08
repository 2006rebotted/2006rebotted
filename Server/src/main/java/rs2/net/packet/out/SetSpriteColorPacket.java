package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetSpriteColorPacket implements Sendable {

	private final int id;
	
	private final int color;
	
	public SetSpriteColorPacket(int id, int color) {
		this.id = id;
		this.color = color;
	}
	
	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(203);
		builder.writeInt(id)
		.writeInt(color);
		return builder.toOutgoingPacket();
	}
	
}
