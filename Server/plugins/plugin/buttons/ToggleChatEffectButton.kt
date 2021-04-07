package plugin.buttons

import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player
import rs2.net.packet.out.SetWidgetConfigPacket

@SubscribesTo(ButtonActionEvent::class)
class ToggleChatEffectButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        player.attr().toggle(Player.CHAT_EFFECTS_KEY);
        player.queuePacket(SetWidgetConfigPacket(172, player.attr().get<Boolean>(Player.CHAT_EFFECTS_KEY)))
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return event.button == 915;
    }

}