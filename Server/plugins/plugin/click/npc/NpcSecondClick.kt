package plugin.click.npc

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.NpcSecondClickEvent

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

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