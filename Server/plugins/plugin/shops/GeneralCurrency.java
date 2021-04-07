package plugin.shops;

import rs2.game.model.entity.mob.player.Player;

/**
 * The parent class of all currencies that provides basic functionality for any
 * general currency. This can be used to create tangible, and even intangible
 * currencies.
 *
 * @author lare96 <http://github.com/lare96>
 */
public interface GeneralCurrency {

    /**
     * The method executed when the currency is taken from {@code player}.
     *
     * @param player
     *            the player the currency is taken from.
     * @param amount
     *            the amount of currency that is taken.
     */
    public void takeCurrency(Player player, int amount);

    /**
     * The method executed when the currency is given to {@code player}.
     *
     * @param player
     *            the player the currency is given to.
     * @param amount
     *            the amount of currency that is given.
     */
    public void recieveCurrency(Player player, int amount);

    /**
     * The method that retrieves the amount of currency {@code player} currently
     * has.
     *
     * @param player
     *            the player who's currency amount will be determined.
     * @return the amount of the currency the player has.
     */
    public int currencyAmount(Player player);

    /**
     * Determines if the currency can be received when {@code player}'s
     * inventory is full.
     *
     * @param player
     *            the player to determine this for.
     * @return {@code true} if the currency can be recieved, {@code false}
     *         otherwise.
     */
    public boolean canRecieveCurrency(Player player);
}
