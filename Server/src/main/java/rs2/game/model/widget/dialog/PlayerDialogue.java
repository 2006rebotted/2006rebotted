package rs2.game.model.widget.dialog;

/**
 * A {@link Chainable} implementation that represents a player talking.
 *
 * @author Vult-R
 */
public class PlayerDialogue implements Chainable {

	/**
	 * The expression of this player.
	 */
	private final Expression expression;

	/**
	 * The text for this dialogue.
	 */
	private final String[] lines;

	/**
	 * Creates a new {@link PlayerDialogue} with a default expression of {@code DEFAULT}.
	 *
	 * @param lines
	 * 		The text for this dialogue.
	 */
	public PlayerDialogue(String... lines) {
		this(Expression.DEFAULT, lines);
	}

	/**
	 * Creates a new {@link PlayerDialogue}.
	 *
	 * @param expression
	 * 		The expression for this dialogue.
	 *
	 * @param lines
	 * 		The text for this dialogue.
	 */
	public PlayerDialogue(Expression expression, String... lines) {
		this.expression = expression;
		this.lines = lines;
	}

	/**
	 * Gets the expression of this player.
	 *
	 * @return The expression of this player.
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Gets the text for this dialogue.
	 *
	 * @return The text.
	 */
	public String[] getLines() {
		return lines;
	}

	@Override
	public void accept(DialogueFactory factory) {
		factory.sendPlayerChat(this);
	}
}

