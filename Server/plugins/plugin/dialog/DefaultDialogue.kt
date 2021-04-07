package plugin.dialog

import rs2.Configuration
import rs2.game.model.widget.dialog.Dialogue
import rs2.game.model.widget.dialog.DialogueFactory
import rs2.game.model.widget.dialog.Expression

/**
 * An implementation of dialogues, to show its syntax.
 *
 * @author Vult-R
 */
open class DefaultDialogue(val npcId: Int): Dialogue() {

    override fun sendDialogues(factory: DialogueFactory) {
        factory.sendNpcChat(npcId, "Hello!", "Welcome to " + Configuration.SERVER_NAME +"!")
        .sendPlayerChat(Expression.HAPPY, "Well hello there stranger!")
        .sendNpcChat(Expression.HAPPY, "Enjoy your stay!", "If you see any bugs, please report them!")
        .execute();
    }

}