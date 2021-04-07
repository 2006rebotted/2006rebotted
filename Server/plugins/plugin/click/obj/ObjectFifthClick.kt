package plugin.click.obj

import astraeus.game.event.impl.ObjectFifthClickEvent
import astraeus.game.event.EventSubscriber
import astraeus.game.event.EventContext
import astraeus.game.event.SubscribesTo

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

@SubscribesTo(ObjectFifthClickEvent::class)
class ObjectFifthClick : EventSubscriber<ObjectFifthClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: ObjectFifthClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ObjectFifthClick] [id= ${event.gameObject.id}], [pos= ${event.gameObject.position.toString()}]"));
        }
		
		when (event.gameObject.id) {
			
		}
		
	}
		
}