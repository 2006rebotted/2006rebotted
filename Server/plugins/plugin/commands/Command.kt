package plugin.commands

import rs2.game.event.impl.CommandEvent
import rs2.game.event.EventSubscriber
import rs2.game.event.EventContext

import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

abstract class Command : EventSubscriber<CommandEvent> {

	override fun subscribe(context: EventContext, player: Player, event: CommandEvent) {
		
		if (player.rights.less(rights)) {
			return
		}
		
		execute(player, CommandParser(event.input))
		
	}
	
	abstract fun execute(player : Player, parser : CommandParser) : Boolean
	
	abstract val rights : PlayerRights

}