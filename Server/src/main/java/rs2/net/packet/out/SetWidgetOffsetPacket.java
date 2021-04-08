package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class SetWidgetOffsetPacket implements Sendable {

	private final int id;
	private final int x, y;

	public SetWidgetOffsetPacket(int x, int y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(70);
		builder.writeShort(x)
		.writeShort(y, ByteOrder.LITTLE)
		.writeShort(id, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
