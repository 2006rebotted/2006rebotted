package plugin.dialog

import astraeus.game.model.widget.dialog.Dialogue
import astraeus.game.model.widget.dialog.DialogueFactory
import astraeus.game.model.widget.dialog.Expression

import plugin.shops.Shops
import plugin.shops.ShopEvent

/**
 * An implementation of dialogues, to show its syntax.
 *
 * @author Vult-R
 */
open class GeneralStoreDialogue(): Dialogue() {

    override fun sendDialogues(factory: DialogueFactory) {
        factory.sendNpcChat("Can I help you at all?")
		.sendOption("Yes please. What are you selling?", Runnable {
			factory.onAction {
				factory.player.post(ShopEvent("General Store"))
			}
		}, "No thanks.", Runnable {
			factory.sendPlayerChat("No thanks.");
		}).execute();
    }

}