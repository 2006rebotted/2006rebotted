package plugin.buttons

import astraeus.game.event.SubscribesTo
import astraeus.game.event.impl.ButtonActionEvent
import astraeus.game.model.entity.mob.player.Player

import astraeus.game.model.widget.dialog.Dialogue

@SubscribesTo(ButtonActionEvent::class)
class DialogueButton : ButtonClick() {
	
	override fun execute(player: Player, event: ButtonActionEvent) {
		player.optionDialogue?.let {
			when (event.button) {
				2461, 2471, 2482, 2494 -> player.dialogueFactory.executeOption(0, player.optionDialogue)
                2495, 2462, 2472, 2483 -> player.dialogueFactory.executeOption(1, player.optionDialogue)
                2496, 2473, 2484 -> player.dialogueFactory.executeOption(2, player.optionDialogue)
                2497, 2485 -> player.dialogueFactory.executeOption(3, player.optionDialogue)
                2498 -> player.dialogueFactory.executeOption(4, player.optionDialogue)
			}
		}
	}

	override fun test(event: ButtonActionEvent): Boolean {
		return Dialogue.isDialogueButton(event.button);
	}
	
}