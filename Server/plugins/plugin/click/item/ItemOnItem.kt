package plugin.click.item

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ItemOnItemEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemOnItemEvent::class)
class ItemOnItem : EventSubscriber<ItemOnItemEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemOnItemEvent) {
		
        if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemOnItem] - used: ${event.used.id} with: ${event.usedWith.id}}"));
        }	
		
		
	}
	
}