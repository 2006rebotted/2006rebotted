package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player

import astraeus.game.model.entity.mob.player.event.LogoutEvent

@SubscribesTo(ButtonActionEvent::class)
class LogoutButton : ButtonClick() {

	override fun execute(player: Player, event: ButtonActionEvent) {
		player.logout()
	}

	override fun test(event: ButtonActionEvent): Boolean {
		return event.button == 2458;
	}

}