package rs2.net.packet.in;

import rs2.game.event.impl.ObjectFifthClickEvent;
import rs2.game.model.Position;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.object.GameObject;
import rs2.game.task.impl.DistancedTask;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.OBJECT_OPTION_5)
public final class ObjectFifthOptionClickPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();
		
		int id = reader.readShort(ByteModification.ADDITION);
		int y = reader.readShort(ByteModification.ADDITION);
		int x = reader.readShort();
		
		//TODO don't create object like this, this is bad
		GameObject object = new GameObject(id, new Position(x, y, player.getPosition().getHeight()));

		if (player == null || object == null || object.getId() != id) {
			return;
		}

		player.startAction(new DistancedTask(player, object.getPosition(), 2) {

			@Override
			public void onReached() {
				player.faceLocation(object.getPosition());
				player.post(new ObjectFifthClickEvent(object));
			}
			
		});

	}

}
