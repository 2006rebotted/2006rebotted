package plugin.commands.impl

import astraeus.game.event.impl.CommandEvent
import astraeus.game.event.SubscribesTo
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights
import astraeus.net.packet.out.ServerMessagePacket


import plugin.commands.Command
import plugin.commands.CommandParser

@SubscribesTo(CommandEvent::class)
class TestCommand : Command() {

	override fun execute(player: Player, parser: CommandParser) : Boolean {
		player.queuePacket(ServerMessagePacket("${parser.command} command works perfectly!"))				
		return true
	}

	override fun test(event: CommandEvent): Boolean {
		when (event.name) {
			"test" -> return true

			else -> return false
		}
	}

	override val rights: PlayerRights
		get() {
			return PlayerRights.PLAYER
		}

}