package plugin.click.item

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ItemOnObjectEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemOnObjectEvent::class)
class ItemOnObject : EventSubscriber<ItemOnObjectEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemOnObjectEvent) {
        if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemOnObject] - itemId:  ${event.item.id} objectId: ${event.gameObject.id} objectLocation: ${event.gameObject.position}"))
        }
		
	}
	
}