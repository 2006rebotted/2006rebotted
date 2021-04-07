package plugin.buttons

import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player
import rs2.net.packet.out.SetWidgetConfigPacket

@SubscribesTo(ButtonActionEvent::class)
class ToggleAcceptAidButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        player.attr().toggle(Player.ACCEPT_AID_KEY);
        player.queuePacket(SetWidgetConfigPacket(427, player.attr().get<Boolean>(Player.ACCEPT_AID_KEY)))
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return event.button == 12464;
    }

}