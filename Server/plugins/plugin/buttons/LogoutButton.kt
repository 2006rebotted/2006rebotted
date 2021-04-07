package plugin.buttons

import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player

@SubscribesTo(ButtonActionEvent::class)
class LogoutButton : ButtonClick() {

	override fun execute(player: Player, event: ButtonActionEvent) {
		player.logout()
	}

	override fun test(event: ButtonActionEvent): Boolean {
		return event.button == 2458;
	}

}