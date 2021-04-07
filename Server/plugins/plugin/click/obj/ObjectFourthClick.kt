package plugin.click.obj

import astraeus.game.event.impl.ObjectFourthClickEvent
import astraeus.game.event.EventSubscriber
import astraeus.game.event.EventContext
import astraeus.game.event.SubscribesTo

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

@SubscribesTo(ObjectFourthClickEvent::class)
class ObjectFourthClick : EventSubscriber<ObjectFourthClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: ObjectFourthClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[click= object], [type= third], [id= ${event.gameObject.id}], [location= ${event.gameObject.position.toString()}]"));
        }
		
		when (event.gameObject.id) {
			
		}
		
	}
	
}