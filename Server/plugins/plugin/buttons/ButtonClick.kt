package plugin.buttons

import rs2.game.event.EventContext
import rs2.game.event.EventSubscriber
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player

abstract class ButtonClick : EventSubscriber<ButtonActionEvent> {

	override fun subscribe(context: EventContext, player: Player, event: ButtonActionEvent) {
		if (!player.canClickButton()) {
			return
		}

		execute(player, event)
	}
	
	abstract fun execute(player : Player, event : ButtonActionEvent);

}