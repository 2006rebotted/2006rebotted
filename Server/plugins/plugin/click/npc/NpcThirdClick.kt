package plugin.click.npc

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.NpcThirdClickEvent

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

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