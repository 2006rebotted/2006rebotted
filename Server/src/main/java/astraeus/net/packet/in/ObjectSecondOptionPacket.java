package astraeus.net.packet.in;

import astraeus.game.event.impl.ObjectSecondClickEvent;
import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.object.GameObject;
import astraeus.game.task.impl.DistancedTask;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.OBJECT_OPTION_2)
public final class ObjectSecondOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		int id = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		int y = reader.readShort(ByteOrder.LITTLE);
		int x = reader.readShort(false, ByteModification.ADDITION);

		GameObject object = new GameObject(id, new Position(x, y, player.getPosition().getHeight()));

		if (player == null || object == null || object.getId() != id) {
			return;
		}

		player.startAction(new DistancedTask(player, object.getPosition(), 2) {

			@Override
			public void onReached() {
				player.faceLocation(object.getPosition());
				player.post(new ObjectSecondClickEvent(object));
			}
			
		});
		
	}

}
