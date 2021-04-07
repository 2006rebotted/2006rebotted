package plugin.click.item

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ItemOnNpcEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemOnNpcEvent::class)
class ItemOnNpc : EventSubscriber<ItemOnNpcEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemOnNpcEvent) {
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemOnNpc] - itemId: ${event.item.id} npcId: ${event.npc.id} slot: ${event.npc.slot}"));
        }
		
	}
	
}