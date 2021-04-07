package plugin.commands.impl

import astraeus.game.event.impl.CommandEvent
import astraeus.game.event.SubscribesTo
import astraeus.game.model.entity.mob.player.Player
import astraeus.game.model.entity.mob.player.PlayerRights
import astraeus.net.packet.out.ServerMessagePacket
import astraeus.game.model.World


import plugin.commands.Command
import plugin.commands.CommandParser

@SubscribesTo(CommandEvent::class)
class ReloadPluginCommand : Command() {

	override fun execute(player: Player, parser: CommandParser) : Boolean {
		player.queuePacket(ServerMessagePacket("Reloading plugins..."))		
		World.world.pluginService.reload()
		player.queuePacket(ServerMessagePacket("Reloaded ${World.world.pluginService.subscribers.size} plugins! "))
		return true
	}

	override fun test(event: CommandEvent): Boolean {
		when(event.name) {			
			"reload" -> return true
			
			else -> return false		
		}
		
	}

	override val rights: PlayerRights
		get() {
			return PlayerRights.PLAYER
		}

}