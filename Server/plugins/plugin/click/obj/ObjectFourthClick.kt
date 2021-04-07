package plugin.click.obj

import rs2.game.event.impl.ObjectFourthClickEvent
import rs2.game.event.EventSubscriber
import rs2.game.event.EventContext
import rs2.game.event.SubscribesTo

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

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