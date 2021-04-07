package plugin.click.obj

import rs2.game.event.impl.ObjectFifthClickEvent
import rs2.game.event.EventSubscriber
import rs2.game.event.EventContext
import rs2.game.event.SubscribesTo

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

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