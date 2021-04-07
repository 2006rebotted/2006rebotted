package plugin.click.obj

import rs2.game.event.impl.ObjectThirdClickEvent
import rs2.game.event.EventSubscriber
import rs2.game.event.EventContext
import rs2.game.event.SubscribesTo

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

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