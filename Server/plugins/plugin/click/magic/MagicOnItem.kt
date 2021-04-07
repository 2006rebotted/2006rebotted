package plugin.click.magic

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.SubscribesTo
import rs2.game.event.impl.MagicOnItemEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import rs2.net.packet.out.ServerMessagePacket

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