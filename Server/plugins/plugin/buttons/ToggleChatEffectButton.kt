package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.net.packet.out.SetWidgetConfigPacket

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