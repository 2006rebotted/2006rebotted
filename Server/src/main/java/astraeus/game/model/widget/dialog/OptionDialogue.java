package astraeus.game.model.widget.dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Chainable} implementation that represents a dialogue in which options are given to the player.
 * 
 * @author Vult-R
 */
public final class OptionDialogue implements Chainable {

	/**
	 * The text for this dialogue.
	 */
	private final String[] lines;

	/**
	 * The list of actions for this dialogue.
	 */
	private final List<Runnable> actions = new ArrayList<>();

	/**
	 * Creates a new {@link OptionDialogue}.
	 * 
	 * @param option1
	 * 		The text for the first option.
	 * 
	 * @param action1
	 * 		The action for the first action.
	 * 
	 * @param option2
	 * 		The text for the second option.
	 * 
	 * @param action2
	 * 		The action for the second action.
	 */
	public OptionDialogue(String option1, Runnable action1, String option2, Runnable action2) {
		lines = new String[] { option1, option2 };
		actions.add(action1);
		actions.add(action2);
	}

	/**
	 * Creates a new {@link OptionDialogue}.
	 * 
	 * @param option1
	 * 		The text for the first option.
	 * 
	 * @param action1
	 * 		The action for the first action.
	 * 
	 * @param option2
	 * 		The text for the second option.
	 * 
	 * @param action2
	 * 		The action for the second action.
	 * 
	 * @param option3
	 * 		The text for the third option.
	 * 
	 * @param action3
	 * 		The action for the third action.
	 */
	public OptionDialogue(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3) {
		lines = new String[] { option1, option2, option3 };
		actions.add(action1);
		actions.add(action2);
		actions.add(action3);
	}

	/**
	 * Creates a new {@link OptionDialogue}.
	 * 
	 * @param option1
	 * 		The text for the first option.
	 * 
	 * @param action1
	 * 		The action for the first action.
	 * 
	 * @param option2
	 * 		The text for the second option.
	 * 
	 * @param action2
	 * 		The action for the second action.
	 * 
	 * @param option3
	 * 		The text for the third option.
	 * 
	 * @param action3
	 * 		The action for the third action.
	 * 
	 * @param option4
	 * 		The text for the four option.
	 * 
	 * @param action4
	 * 		The action for the four action.
	 */
	public OptionDialogue(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3, String option4, Runnable action4) {
		lines = new String[] { option1, option2, option3, option4 };
		actions.add(action1);
		actions.add(action2);
		actions.add(action3);
		actions.add(action4);
	}

	/**
	 * Creates a new {@link OptionDialogue}.
	 * 
	 * @param option1
	 * 		The text for the first option.
	 * 
	 * @param action1
	 * 		The action for the first action.
	 * 
	 * @param option2
	 * 		The text for the second option.
	 * 
	 * @param action2
	 * 		The action for the second action.
	 * 
	 * @param option3
	 * 		The text for the third option.
	 * 
	 * @param action3
	 * 		The action for the third action.
	 * 
	 * @param option4
	 * 		The text for the four option.
	 * 
	 * @param action4
	 * 		The action for the four action.
	 * 
	 * @param option5
	 * 		The text for the fifth option.
	 * 
	 * @param action5
	 * 		The action for the fifth action.
	 */
	public OptionDialogue(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3, String option4, Runnable action4, String option5, Runnable action5) {
		lines = new String[] { option1, option2, option3, option4, option5 };
		actions.add(action1);
		actions.add(action2);
		actions.add(action3);
		actions.add(action4);
		actions.add(action5);
	}

	@Override
	public void accept(DialogueFactory factory) {
		factory.sendOption(this);
	}

	/**
	 * Gets the text for this dialogue.
	 * 
	 * @return The text.
	 */
	public String[] getLines() {
		return lines;
	}

	/**
	 * Gets the list of actions for this dialogue.
	 * 
	 * @return The list of actions.
	 */
	public List<Runnable> getActions() {
		return actions;
	}
}