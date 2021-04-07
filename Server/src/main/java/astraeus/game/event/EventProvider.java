package astraeus.game.event;

import astraeus.game.model.entity.mob.player.Player;

/**
 * An event provider provides support for dynamic {@link Event} posting,
 * depriving and providing {@link EventSubscriber}s
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public interface EventProvider {

	/**
	 * Provides an {@link EventSubscriber} for the specified event.
	 *
	 * @param subscriber The subscriber to provide.
	 */
	void provideSubscriber(EventSubscriber<?> subscriber);

	/**
	 * Deprives an {@link EventSubscriber} for the specified event.
	 *
	 * @param subscriber The subscriber to deprive.
	 */
	void depriveSubscriber(EventSubscriber<?> subscriber);

	/**
	 * Posts an {@link Event}, notifying all provided subscribers.
	 *
	 * @param <E> The event type reference.
	 * @param player The player to post the event for.
	 * @param event The event to post.
	 */
	<E extends Event> void post(Player player, E event);

}
