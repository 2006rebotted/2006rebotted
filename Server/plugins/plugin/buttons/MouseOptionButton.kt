package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.net.packet.out.SetWidgetConfigPacket

@SubscribesTo(ButtonActionEvent::class)
class MouseOptionButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        player.attr().toggle(Player.MOUSE_BUTTON_KEY);
        player.queuePacket(SetWidgetConfigPacket(171, player.attr().get(Player.MOUSE_BUTTON_KEY)))
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return event.button == 914 ;
    }

}