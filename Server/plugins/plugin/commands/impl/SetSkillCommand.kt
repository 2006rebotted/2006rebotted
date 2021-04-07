package plugin.commands.impl

import astraeus.game.event.impl.CommandEvent
import astraeus.game.event.SubscribesTo
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights
import astraeus.net.packet.out.ServerMessagePacket
import astraeus.game.model.World
import astraeus.net.packet.out.UpdateSkillPacket

import plugin.commands.Command
import plugin.commands.CommandParser

@SubscribesTo(CommandEvent::class)
class SetSkillCommand : Command() {

    override fun execute(player: Player, parser: CommandParser): Boolean {

        if (parser.hasNext(2)) {
            val skill = parser.nextInt()

            var lvl = parser.nextInt()

            if (skill < 0 || skill > 22) {
                return false
            }

            if (skill == 3) {
                if (lvl <= 10) {
                    lvl = 10
                }
            }

            if (lvl < 1) {
                lvl = 1
            } else if (lvl > 99) {
                lvl = 99
            }

            player.skills.setMaxLevel(skill, lvl)
            return true
        }

        return false
    }

    override fun test(event: CommandEvent): Boolean {
        when (event.name) {
            "lvl" -> return true

            else -> return false
        }

    }

    override val rights: PlayerRights
        get() {
            return PlayerRights.DEVELOPER
        }


}