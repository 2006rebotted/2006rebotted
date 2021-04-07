package plugin.shops;

/**
 * The enumerated type whose elements represent all of the different currencies
 * that can be used with shops.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum Currency {
    COINS(new ItemCurrency(995)),
    TOKKUL(new ItemCurrency(6529)),
    CASTLE_WARS_TICKETS(new ItemCurrency(4067)),
    AGILITY_ARENA_TICKETS(new ItemCurrency(2996));

    /**
     * The currency that is represented by this element.
     */
    private final GeneralCurrency currency;

    /**
     * Creates a new {@link Currency}.
     *
     * @param currency
     *            the currency that is represented by this element.
     */
    private Currency(GeneralCurrency currency) {
        this.currency = currency;
    }

    @Override
    public final String toString() {
        return name().toLowerCase().replaceAll("_", " ");
    }

    /**
     * Gets the currency that is represented by this element.
     *
     * @return the currency that is represented.
     */
    public final GeneralCurrency getCurrency() {
        return currency;
    }
}
