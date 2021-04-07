package plugin.click.obj

import astraeus.game.event.impl.ObjectSecondClickEvent
import astraeus.game.event.EventSubscriber
import astraeus.game.event.EventContext
import astraeus.game.event.SubscribesTo

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

@SubscribesTo(ObjectSecondClickEvent::class)
class ObjectSecondClick : EventSubscriber<ObjectSecondClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: ObjectSecondClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[click= object], [type= second], [id= ${event.gameObject.id}], [location= ${event.gameObject.position.toString()}]"));
        }
		
		println(event.gameObject.id)
		
		when (event.gameObject.id) {
			
			2213 -> player.bank.open()
			
		}
		
	}
	
}