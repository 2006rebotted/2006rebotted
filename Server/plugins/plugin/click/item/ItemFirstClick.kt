package plugin.click.item

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ItemFirstClickEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemFirstClickEvent::class)
class ItemFirstClick : EventSubscriber<ItemFirstClickEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemFirstClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemClick#1] - Item: ${event.item} WidgetId: ${event.widgetId}"))
        }
		
		when(event.item.id) {

		}
		
		
	}
	
}