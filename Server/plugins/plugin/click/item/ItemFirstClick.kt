package plugin.click.item

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ItemFirstClickEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.SetWidgetConfigPacket
import astraeus.net.packet.out.ServerMessagePacket

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