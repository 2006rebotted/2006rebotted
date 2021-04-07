package plugin.click.obj

import astraeus.game.event.impl.ObjectFirstClickEvent
import astraeus.game.event.EventSubscriber
import astraeus.game.event.EventContext
import astraeus.game.event.SubscribesTo

import astraeus.net.packet.out.ServerMessagePacket

import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.game.event.impl.DoorEvent

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