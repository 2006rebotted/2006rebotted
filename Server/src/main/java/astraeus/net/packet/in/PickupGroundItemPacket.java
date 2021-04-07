package astraeus.net.packet.in;

import astraeus.game.model.Position;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.PlayerRights;
import astraeus.game.model.entity.object.GameObjects;
import astraeus.game.task.impl.DistancedTask;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.out.RemoveGroundItemPacket;
import astraeus.net.packet.out.ServerMessagePacket;

/**
 * The {@link IncomingPacket} responsible for picking up an item on the ground.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode(IncomingPacket.PICKUP_GROUND_ITEM)
public final class PickupGroundItemPacket implements Receivable {

	@Override
	public void handlePacket(final Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		final int y = reader.readShort(ByteOrder.LITTLE);
		final int id = reader.readShort(false);
		final int x = reader.readShort(ByteOrder.LITTLE);

		// create the position object
		Position position = new Position(x, y, player.getPosition().getHeight());

		// get the item from the map
		Item item = GameObjects.getGroundItems().get(position);

		// validate it exists
		if (item == null) {
			return;
		}
		
		// validate the item is the same item
		if (item.getId() != id) {
			return;
		}

		if (player.getRights().equals(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
			player.queuePacket(new ServerMessagePacket(
					String.format("[PickupItem] - Item: %s Position: %s", item.toString(), position.toString())));
		}
		
		player.startAction(new DistancedTask(player, position, 2) {

			@Override
			public void onReached() {
				player.getInventory().add(item);
				player.queuePacket(new RemoveGroundItemPacket(item));
				GameObjects.getGlobalObjects().remove(item);
			}
			
		});
		
	}
}
