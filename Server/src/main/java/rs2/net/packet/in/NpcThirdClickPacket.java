package rs2.net.packet.in;

import rs2.game.event.impl.NpcThirdClickEvent;
import rs2.game.model.World;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.task.impl.DistancedTask;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode({ IncomingPacket.NPC_OPTION_3 })
public final class NpcThirdClickPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		final Npc npc = World.world.getMobs().get(packet.getReader().readShort());

		if (npc == null) {
			return;
		}
		
		player.startAction(new DistancedTask(player, npc.getPosition(), 2) {

			@Override
			public void onReached() {
				player.setInteractingEntity(npc);
				npc.setInteractingEntity(player);
				player.post(new NpcThirdClickEvent(npc));
				stop();
				
			}

		});
		
	}

}
