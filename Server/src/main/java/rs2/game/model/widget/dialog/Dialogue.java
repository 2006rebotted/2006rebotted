package rs2.game.model.widget.dialog;

import com.google.common.collect.ImmutableList;

/**
 * Represents an abstract dialogue, in which extending classes will be able to construct and send dialogues
 * to a player.
 * 
 * @author Vult-R
 */
public abstract class Dialogue {

    /**
     * The action buttons responsible for dialogues.
     */
    public static final ImmutableList<Integer> DIALOGUE_BUTTONS = ImmutableList.of(2461, 2471, 2482, 2462, 2472, 2483, 2473, 2484, 2485, 2494, 2495, 2496, 2497, 2498);

    /**
     * Sends a player a dialogue.
     *
     * @param factory
     *      The factory for this dialogue.
     */
    public abstract void sendDialogues(DialogueFactory factory);

    /**
     * Checks if the button triggered is an optional dialogue button.
     *
     * @param button
     *            The index of the button being checked.
     *
     * @return The result of the operation.
     */
    public static final boolean isDialogueButton(int button) {
        return DIALOGUE_BUTTONS.stream().anyMatch(search -> search == button);
    }

}
