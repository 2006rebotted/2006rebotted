package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.net.packet.out.SetWidgetConfigPacket

import astraeus.game.model.entity.mob.Movement

@SubscribesTo(ButtonActionEvent::class)
class ToggleRunButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        when (event.button) {
            1050, 19158 -> {
				
				player.attr().toggle(Movement.RUNNING_KEY)             
				
				val running = player.attr().get(Movement.RUNNING_KEY)
				
                // run orb toggle
                player.queuePacket(SetWidgetConfigPacket(152, if (running) 1 else 0))
                // run button in the wrench tab
                player.queuePacket(SetWidgetConfigPacket(429, if (running) 1 else 0))			

            }
        }
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return event.button == 1050 || event.button == 19158;
    }

}