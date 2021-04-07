package plugin.click.npc

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.NpcSecondClickEvent

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

@SubscribesTo(NpcSecondClickEvent::class)
class NpcSecondClick : EventSubscriber<NpcSecondClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: NpcSecondClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[click= npc], [type = second], [id= ${event.npc.id}], [slot= ${event.npc.slot}]"));
        }
		
		when(event.npc.id) {
			
			494 -> player.bank.open()
			
		}
		
	}
	
}