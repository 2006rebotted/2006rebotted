package plugin.click.item

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ItemOnNpcEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.SetWidgetConfigPacket
import astraeus.net.packet.out.ServerMessagePacket

@SubscribesTo(ItemOnNpcEvent::class)
class ItemOnNpc : EventSubscriber<ItemOnNpcEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: ItemOnNpcEvent) {
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[ItemOnNpc] - itemId: ${event.item.id} npcId: ${event.npc.id} slot: ${event.npc.slot}"));
        }
		
	}
	
}