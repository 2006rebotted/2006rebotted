package rs2.net.packet.in;

import rs2.game.event.impl.ObjectThirdClickEvent;
import rs2.game.model.Position;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.object.GameObject;
import rs2.game.task.impl.DistancedTask;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.OBJECT_OPTION_3)
public final class ObjectThirdOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		int x = reader.readShort(ByteOrder.LITTLE);
		int y = reader.readShort(false);
		int id = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);

		GameObject object = new GameObject(id, new Position(x, y, player.getPosition().getHeight()));

		if (player == null || object == null || object.getId() != id) {
			return;
		}

		player.startAction(new DistancedTask(player, object.getPosition(), 2) {

			@Override
			public void onReached() {
				player.faceLocation(object.getPosition());
				player.post(new ObjectThirdClickEvent(object));
			}
			
		});
		
	}

}
