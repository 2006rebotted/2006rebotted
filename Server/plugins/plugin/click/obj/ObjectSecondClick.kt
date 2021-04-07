package plugin.click.obj

import rs2.game.event.impl.ObjectSecondClickEvent
import rs2.game.event.EventSubscriber
import rs2.game.event.EventContext
import rs2.game.event.SubscribesTo

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

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