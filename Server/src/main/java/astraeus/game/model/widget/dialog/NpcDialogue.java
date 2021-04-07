package astraeus.game.model.widget.dialog;

/**
 * The {@link Chainable} implementation that represents dialogue in which an NPC is talking.
 *
 * @author Vult-R
 */
public final class NpcDialogue implements Chainable {

	/**
	 * The id of this npc.
	 */
	private int id = -1;

	/**
	 * The expression of this NPC.
	 */
	private final Expression expression;

	/**
	 * The text for this dialogue.
	 */
	private final String[] lines;

	/**
	 * Creates a new {@link NpcDialogue}
	 *
	 * @param lines
	 * 		The text for this dialogue.
	 */
	public NpcDialogue(String... lines) {
		this(Expression.DEFAULT, lines);
	}

	/**
	 * Creates a new {@link NpcDialogue}
	 *
	 * @param expression
	 * 		The expression of this npc.
	 *
	 * 	@param lines
	 * 		The text for this dialogue.
	 */
	public NpcDialogue(Expression expression, String... lines) {
		this.expression = expression;
		this.lines = lines;
	}

	/**
	 * Creates a new {@link NpcDialogue}
	 *
	 * @param id
	 * 		The id of this npc.
	 *
	 * @param lines
	 * 		The text for this dialogue.
	 */
	public NpcDialogue(int id, String... lines) {
		this(id, Expression.DEFAULT, lines);
	}

	/**
	 * Creates a new {@link NpcDialogue}
	 *
	 * @param id
	 * 		The id of this npc.
	 *
	 * @param expression
	 * 		The expression of this npc.
	 *
	 * 	@param lines
	 * 		The text for this dialogue.
	 */
	public NpcDialogue(int id, Expression expression, String... lines) {
		this.id = id;
		this.expression = expression;
		this.lines = lines;
	}

	/**
	 * Gets the id of this npc.
	 *
	 * @return The id of this npc.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the expression of this npc.
	 *
	 * @return The expression.
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
		factory.sendNpcChat(this);
	}
}

