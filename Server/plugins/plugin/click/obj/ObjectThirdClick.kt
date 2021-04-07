package plugin.click.obj

import astraeus.game.event.impl.ObjectThirdClickEvent
import astraeus.game.event.EventSubscriber
import astraeus.game.event.EventContext
import astraeus.game.event.SubscribesTo

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

@SubscribesTo(ObjectThirdClickEvent::class)
class ObjectThirdClick : EventSubscriber<ObjectThirdClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: ObjectThirdClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[click= object], [type= third], [id= ${event.gameObject.id}], [location= ${event.gameObject.position.toString()}]"));
        }
		
		when (event.gameObject.id) {
			
		}
		
	}
	
}