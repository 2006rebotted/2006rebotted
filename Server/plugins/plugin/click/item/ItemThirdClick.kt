package plugin.click.item

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ItemThirdClickEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemThirdClickEvent::class)
class ItemThirdClick : EventSubscriber<ItemThirdClickEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemThirdClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemClick#3] - ItemId: ${event.id} Slot: ${event.slot} WidgetId: ${event.widgetId}"))
        }
		
		when(event.id) {

		}
		
		
	}
	
}