package plugin.click.obj

import rs2.game.event.impl.ObjectFirstClickEvent
import rs2.game.event.EventSubscriber
import rs2.game.event.EventContext
import rs2.game.event.SubscribesTo

import rs2.net.packet.out.ServerMessagePacket

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.game.event.impl.DoorEvent

import plugin.doors.DoorUtils

@SubscribesTo(ObjectFirstClickEvent::class)
class ObjectFirstClick : EventSubscriber<ObjectFirstClickEvent> {
	
	override fun subscribe(context: EventContext, player: Player, event: ObjectFirstClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[click= object], [type= first], [id= ${event.gameObject.id}], [location= ${event.gameObject.position.toString()}]"));
        }
		
		if (DoorUtils.isDoor(event.gameObject.id)) {
			player.post(DoorEvent(event.gameObject))
			return
		}
		
		when (event.gameObject.id) {
			
			2213 -> player.bank.open()
			
		}
		
	}
	
}