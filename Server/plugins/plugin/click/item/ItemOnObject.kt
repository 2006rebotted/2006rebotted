package plugin.click.item

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ItemOnObjectEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.SetWidgetConfigPacket
import astraeus.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemOnObjectEvent::class)
class ItemOnObject : EventSubscriber<ItemOnObjectEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemOnObjectEvent) {
        if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemOnObject] - itemId:  ${event.item.id} objectId: ${event.gameObject.id} objectLocation: ${event.gameObject.position}"))
        }
		
	}
	
}