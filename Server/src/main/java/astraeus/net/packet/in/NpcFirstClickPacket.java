package astraeus.net.packet.in;

import astraeus.game.event.impl.NpcFirstClickEvent;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.task.impl.DistancedTask;
import astraeus.net.codec.ByteOrder;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode({ IncomingPacket.NPC_OPTION_1 })
public final class NpcFirstClickPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final Npc npc = World.world.getMobs().get(packet.getReader().readShort(ByteOrder.LITTLE));

		if (npc == null) {
			return;
		}

		player.startAction(new DistancedTask(player, npc.getPosition(), 3) {

			@Override
			public void onReached() {
				player.setInteractingEntity(npc);
				npc.setInteractingEntity(player);
				player.post(new NpcFirstClickEvent(npc));
				stop();
				
			}

		});

	}

}
