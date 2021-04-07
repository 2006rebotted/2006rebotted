package rs2.game.event;

/**
 * Represents the context of an {@link Event}
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public interface EventContext {

	/**
	 * Breaks the chain of subscribers.
	 */
	void breakSubscriberChain();

	/**
	 * Repairs the chain of subscribers.
	 */
	void repairSubscriberChain();

	/**
	 * Checks whether or not the subscriber chain is broken.
	 *
	 * @return {@code true} if and only if the chain is broken, otherwise
	 *         {@code false}.
	 */
	boolean isChainBroken();

}
