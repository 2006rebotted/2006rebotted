package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.Brightness

import astraeus.net.packet.out.SetWidgetConfigPacket

@SubscribesTo(ButtonActionEvent::class)
class BrightnessButton : ButtonClick() {

	override fun execute(player: Player, event: ButtonActionEvent) {
		when (event.button) {
			906 -> {
				player.attr().put(Player.BRIGHTNESS_KEY, Brightness.VERY_DARK)
				player.queuePacket(SetWidgetConfigPacket(166, player.attr().get(Player.BRIGHTNESS_KEY).code))
			}

			908 -> {
				player.attr().put(Player.BRIGHTNESS_KEY, Brightness.DARK)
				player.queuePacket(SetWidgetConfigPacket(166, player.attr().get(Player.BRIGHTNESS_KEY).code))
			}

			910 -> {
				player.attr().put(Player.BRIGHTNESS_KEY, Brightness.NORMAL)
				player.queuePacket(SetWidgetConfigPacket(166, player.attr().get(Player.BRIGHTNESS_KEY).code))
			}

			912 -> {
				player.attr().put(Player.BRIGHTNESS_KEY, Brightness.BRIGHT)
				player.queuePacket(SetWidgetConfigPacket(166, player.attr().get(Player.BRIGHTNESS_KEY).code))
			}
		}
	}

	override fun test(event: ButtonActionEvent): Boolean {
		return event.button == 906 || event.button == 908 || event.button == 910 || event.button == 912;
	}

}