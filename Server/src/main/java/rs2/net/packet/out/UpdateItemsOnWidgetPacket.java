package rs2.net.packet.out;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.PacketHeader;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class UpdateItemsOnWidgetPacket implements Sendable {

	private final int id;

	private final Item[] items;

	public UpdateItemsOnWidgetPacket(int id, Item... items) {
		this.id = id;
		this.items = items;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(53, PacketHeader.VARIABLE_SHORT);
		builder.writeShort(id);
		
		if (items == null) {
			builder.writeShort(0)
			.write(0)
			.writeShort(0, ByteModification.ADDITION, ByteOrder.LITTLE);
			return builder.toOutgoingPacket();
		}
		
		builder.writeShort(items.length);

		for (final Item item : items) {
			if (item != null) {
				if (item.getAmount() > 254) {
					builder
					.write(255)
					.writeInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
				} else {
					builder.write(item.getAmount());
				}
				builder.writeShort(item.getId() + 1, ByteModification.ADDITION, ByteOrder.LITTLE);
			} else {
				builder.write(0)
				.writeShort(0, ByteModification.ADDITION, ByteOrder.LITTLE);
			}
		}
		return builder.toOutgoingPacket();
	}

}
