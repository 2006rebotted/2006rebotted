package rs2.game.model.entity.mob.player;

/**
 * The class that models a players chat message.
 * 
 * @author Seven
 */
public final class ChatMessage {

    /**
     * The color of the text.
     */
    private final int color;

    /**
     * The client-sided effects.
     */
    private final int effect;

    /**
     * The array of characters.
     */
    private final byte[] text;

    /**
     * A default constructor for new players.
     */
    public ChatMessage() {
	this(0, 0, null);
    }

    /**
     * Creates a new {@link ChatMessage).
     * 
     * @param color
     *            The color of this chat message.
     * 
     * @param effect
     *            The client-sided effect of this message.
     * 
     * @param text
     *            The text in data form.
     */
    public ChatMessage(int color, int effect, byte[] text) {
	this.color = color;
	this.effect = effect;
	this.text = text;
    }

    /**
     * @return the color
     */
    public int getColor() {
	return color;
    }

    /**
     * @return the effect
     */
    public int getEffect() {
	return effect;
    }

    /**
     * @return the textData
     */
    public byte[] getText() {
	return text;
    }

}
