package plugin.buttons

import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player
import rs2.net.packet.out.SetWidgetConfigPacket

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