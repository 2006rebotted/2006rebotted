package astraeus.net.packet.in;

import astraeus.game.event.impl.NpcSecondClickEvent;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.task.impl.DistancedTask;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode({ IncomingPacket.NPC_OPTION_2 })
public final class NpcSecondClickPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final Npc npc = World.world.getMobs().get(packet.getReader().readShort(ByteOrder.LITTLE,
				ByteModification.ADDITION));

		if (npc == null) {
			return;
		}
		
		player.startAction(new DistancedTask(player, npc.getPosition(), 2) {

			@Override
			public void onReached() {
				player.setInteractingEntity(npc);
				npc.setInteractingEntity(player);
				player.post(new NpcSecondClickEvent(npc));
				stop();
				
			}

		});
		
	}

}
