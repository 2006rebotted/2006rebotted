package plugin.commands.impl

import rs2.game.event.impl.CommandEvent
import rs2.game.event.SubscribesTo
import rs2.game.model.entity.mob.player.Player
import rs2.game.model.entity.mob.player.PlayerRights

import plugin.commands.Command
import plugin.commands.CommandParser

@SubscribesTo(CommandEvent::class)
class DisplayWidgetCommand : Command() {
	
	override fun execute(player: Player, parser: CommandParser) : Boolean {
		
		if (parser.hasNext(1)) {
			val id = parser.nextInt()
			
			player.widgets.open(id)
			return true
		} else if (parser.hasNext(2)) {
			val type = parser.nextString();
			
			val id = parser.nextInt()
			
			when (type) {
				"window", "w" -> player.widgets.open(id)
				"chat", "c" -> player.widgets.openChatBoxWidget(id)		
			}
			return true
		} else if (parser.hasNext(3)) {
			val type = parser.nextString()
			
			val id = parser.nextInt()
			
			val sidebarId = parser.nextInt()
			
			when (type) {
				"tab", "t" -> player.widgets.openInventoryWidget(id, sidebarId)
			}
			return true
		}
		return false
	}
	
		override fun test(event: CommandEvent): Boolean {
		when (event.name) {
			"widget" -> return true

			else -> return false
		}
	}

	override val rights: PlayerRights
		get() {
			return PlayerRights.DEVELOPER
		}
	
}