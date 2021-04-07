package astraeus.net.packet.in;

import astraeus.game.event.impl.ObjectFirstClickEvent;
import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.object.GameObject;
import astraeus.game.task.impl.DistancedTask;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.ByteBufReader;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.OBJECT_OPTION_1)
public final class ObjectFirstOptionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		int x = reader.readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
		int id = reader.readShort(false);
		int y = reader.readShort(false, ByteModification.ADDITION);

		GameObject object = new GameObject(id, new Position(x, y));

		if (player == null || object == null) {
			return;
		}
		
		player.startAction(new DistancedTask(player, object.getPosition(), 2) {

			@Override
			public void onReached() {
				player.faceLocation(object.getPosition());
				player.post(new ObjectFirstClickEvent(object));
			}
			
		});
		
	}

}
