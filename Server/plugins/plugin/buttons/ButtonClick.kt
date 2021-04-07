package plugin.buttons

import astraeus.game.event.EventContext
import astraeus.game.event.EventSubscriber
import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights

import astraeus.net.packet.out.ServerMessagePacket

abstract class ButtonClick : EventSubscriber<ButtonActionEvent> {

	override fun subscribe(context: EventContext, player: Player, event: ButtonActionEvent) {
		if (!player.canClickButton()) {
			return
		}

		execute(player, event)
	}
	
	abstract fun execute(player : Player, event : ButtonActionEvent);

}