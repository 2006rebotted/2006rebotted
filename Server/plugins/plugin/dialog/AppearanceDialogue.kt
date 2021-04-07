package plugin.dialog

import astraeus.game.model.widget.dialog.Dialogue
import astraeus.game.model.widget.dialog.DialogueFactory
import astraeus.game.model.widget.dialog.Expression

import astraeus.game.model.entity.mob.player.Player

class AppearanceDialogue : Dialogue() {
	
	override fun sendDialogues(factory: DialogueFactory) {
        factory.sendNpcChat(Expression.HAPPY, "Would you care to change your appearance?")
                .sendOption("Change your appearance", Runnable {
                    factory.sendPlayerChat("I would like to change my appearance.")
                    .onAction(Runnable {
                        factory.player.attr().put(Player.CHANGING_APPEARANCE_KEY, true)
                        factory.player.widgets.open(3559)
                    })
                }, "Keep your current appearance.", Runnable {
                    factory.sendPlayerChat("I would like to keep my current appearance.")

                }).execute()
	}
	
}