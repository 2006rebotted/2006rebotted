package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.net.packet.out.SetWidgetConfigPacket

@SubscribesTo(ButtonActionEvent::class)
class ToggleSplitChatButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        player.attr().toggle(Player.SPLIT_CHAT_KEY);
        player.queuePacket(SetWidgetConfigPacket(287, player.attr().get<Boolean>(Player.SPLIT_CHAT_KEY)))
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return event.button == 957;
    }

}