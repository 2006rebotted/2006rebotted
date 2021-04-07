package plugin.click.npc

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.NpcThirdClickEvent

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

@SubscribesTo(NpcThirdClickEvent::class)
class NpcThirdClick : EventSubscriber<NpcThirdClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: NpcThirdClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[click= npc], [type = third], [id= ${event.npc.id}], [slot= ${event.npc.slot}]"));
        }
		
		when(event.npc.id) {
			
		}
		
	}
	
}