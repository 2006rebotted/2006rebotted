package plugin.buttons

import rs2.game.event.SubscribesTo
import rs2.game.event.impl.ButtonActionEvent
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.sound.Volume
import rs2.net.packet.out.SetWidgetConfigPacket

@SubscribesTo(ButtonActionEvent::class)
class SoundEffectVolumeButton : ButtonClick() {

    override fun execute(player: Player, event: ButtonActionEvent) {
        when (event.button) {
            941 -> {
                player.attr().put(Player.SOUND_EFFECT_VOLUME_KEY, Volume.SILENT)
                player.queuePacket(SetWidgetConfigPacket(169, player.attr().get(Player.SOUND_EFFECT_VOLUME_KEY).code))
            }

            942 -> {
                player.attr().put(Player.SOUND_EFFECT_VOLUME_KEY, Volume.QUIET)
                player.queuePacket(SetWidgetConfigPacket(169, player.attr().get(Player.SOUND_EFFECT_VOLUME_KEY).code))
            }

            943 -> {
                player.attr().put(Player.SOUND_EFFECT_VOLUME_KEY, Volume.NORMAL)
                player.queuePacket(SetWidgetConfigPacket(169, player.attr().get(Player.SOUND_EFFECT_VOLUME_KEY).code))
            }

            944 -> {
                player.attr().put(Player.SOUND_EFFECT_VOLUME_KEY, Volume.HIGH)
                player.queuePacket(SetWidgetConfigPacket(169, player.attr().get(Player.SOUND_EFFECT_VOLUME_KEY).code))
            }

            945 -> {
                player.attr().put(Player.SOUND_EFFECT_VOLUME_KEY, Volume.LOUD)
                player.queuePacket(SetWidgetConfigPacket(169, player.attr().get(Player.SOUND_EFFECT_VOLUME_KEY).code))
            }
        }
    }

    override fun test(event: ButtonActionEvent): Boolean {
        return event.button == 941 || event.button == 942 || event.button == 943 || event.button == 944 || event.button == 945;
    }

}