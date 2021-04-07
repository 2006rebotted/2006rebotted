package rs2.net.packet.in;

import rs2.game.model.Position;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.game.model.entity.object.GameObjects;
import rs2.game.task.impl.DistancedTask;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.out.RemoveGroundItemPacket;
import rs2.net.packet.out.ServerMessagePacket;

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
