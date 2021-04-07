package plugin.commands.impl

import rs2.game.event.impl.CommandEvent
import rs2.game.event.SubscribesTo
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights
import rs2.net.packet.out.ServerMessagePacket

import plugin.commands.Command
import plugin.commands.CommandParser

@SubscribesTo(CommandEvent::class)
class PositionCommand : Command() {

	override fun execute(player: Player, parser: CommandParser) : Boolean {
		player.queuePacket(ServerMessagePacket("Your position is: ${player.position}"))
		return true	
	}

	override fun test(event: CommandEvent): Boolean {		
		when(event.name) {			
			"pos", "mypos" -> return true
			
			else -> return false			
		}

	}

	override val rights: PlayerRights
		get() {
			return PlayerRights.PLAYER
		}

}