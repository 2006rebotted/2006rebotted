package plugin.click.item

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ItemThirdClickEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.SetWidgetConfigPacket
import astraeus.net.packet.out.ServerMessagePacket

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