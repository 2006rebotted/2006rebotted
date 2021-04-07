package plugin.click.magic

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.MagicOnItemEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.SetWidgetConfigPacket
import astraeus.net.packet.out.ServerMessagePacket

@SubscribesTo(MagicOnItemEvent::class)
class MagicOnItem : EventSubscriber<MagicOnItemEvent> {	

	override fun subscribe(context: EventContext, player: Player, event: MagicOnItemEvent) {
		if (player.rights.greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
            player.queuePacket(ServerMessagePacket("[MagicOnItem] - ItemId: ${event.itemId} Slot: ${event.slot} ChildId: ${event.childId} SpellId: ${event.spellId}"));
        }
		
		when(event.spellId) {
			
		}
		
	}
	
}