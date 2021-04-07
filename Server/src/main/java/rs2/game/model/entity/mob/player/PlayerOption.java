package rs2.game.model.entity.mob.player;

/**
 * Represents the options for right-clicking players.
 *
 * @author Seven
 */
public enum PlayerOption  {

    /**
     * The option for challenging another player to a duel.
     */
    DUEL_REQUEST(1, "Challenge"),

    /**
     * The option for attacking another player.
     */
    ATTACK(2, "Attack"),

    /**
     * The option for following another player.
     */
    FOLLOW(3, "Follow"),

    /**
     * The option for trading another player.
     */
    TRADE_REQUEST(4, "Trade with"),

    /**
     * The option for moderators and staff which brings up the report abuse option.
     */
    REPORT(5, "Report"),

    /**
     * The option for special items such as a rubby chicken.
     */
    WACK(5, "Wack");

    /**
     * The id of this option as seen by the client.
     */
    private final int slot;

    /**
     * The name of this option as seen by the client.
     */
    private final String name;

    /**
     * Creates a new {@link PlayerOption}.
     *
     * @param slot
     * 		The id of this option as seen by the client.
     *
     * @param name
     * 		The name of this option as seen by the client.
     */
    private PlayerOption(int slot, String name) {
        this.slot = slot;
        this.name = name;
    }

    /**
     * Gets the slot of this option.
     *
     * @return The slot.
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Gets the name of this slot.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

}

