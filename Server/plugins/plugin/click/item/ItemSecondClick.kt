package plugin.click.item

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ItemSecondClickEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

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