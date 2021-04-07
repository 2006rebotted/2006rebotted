package plugin.click.item

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ItemSecondClickEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.SetWidgetConfigPacket
import astraeus.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemSecondClickEvent::class)
class ItemSecondClick : EventSubscriber<ItemSecondClickEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemSecondClickEvent) {
		
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemClick#2] - ItemId: ${event.id} Slot: ${event.slot}"))
        }
		
		when(event.id) {

		}
		
		
	}
	
}