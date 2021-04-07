package rs2.game.event;

/**
 * An universal implementation of an {@link EventContext}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class UniversalEventContext implements EventContext {

	/**
	 * A flag denoting whether or not the subscriber chain is broken.
	 */
	private boolean chainBroken;

	@Override
	public void breakSubscriberChain() {
		chainBroken = true;
	}

	@Override
	public void repairSubscriberChain() {
		chainBroken = false;
	}

	@Override
	public boolean isChainBroken() {
		return chainBroken;
	}

}
