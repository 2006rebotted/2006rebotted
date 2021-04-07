package rs2.net.packet.in;

import rs2.game.event.impl.ItemFirstClickEvent;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ITEM_OPTION_1)
public final class ItemFirstOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final ByteBufReader reader = packet.getReader();
		final int widgetId = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		final int slot = reader.readShort(false, ByteModification.ADDITION);
		final int id = reader.readShort(ByteOrder.LITTLE);
		
		final Item item = player.getInventory().get(slot);
		
		if (item.getId() != id) {
			return;
		}

		player.post(new ItemFirstClickEvent(item, widgetId));
	}

}
