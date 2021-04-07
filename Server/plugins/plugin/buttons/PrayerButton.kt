package plugin.buttons

import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.Prayer

@SubscribesTo(ButtonActionEvent::class)
class PrayerButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        player.prayer.clickButton(event.button)
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return Prayer.isPrayerButton(event.button)
    }

}