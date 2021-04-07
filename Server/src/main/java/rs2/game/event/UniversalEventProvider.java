package rs2.game.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import rs2.game.model.entity.mob.player.Player;
import rs2.util.ClassUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A universal event provider which posts, provides and deprives subscribers.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class UniversalEventProvider implements EventProvider {

	/**
	 * A {@link Multimap} of {@link Event} classes to subscribers.
	 */
	private final Multimap<Class<? extends Event>, EventSubscriber<? super Event>> events = ArrayListMultimap.create();

	/**
	 * The universal context of this event provider.
	 */
	private final EventContext context = new UniversalEventContext();

	@SuppressWarnings("unchecked")
	@Override
	public void provideSubscriber(EventSubscriber<?> subscriber) {
		checkSubscriber(subscriber, annotation -> events.put(annotation.value(), (EventSubscriber<? super Event>) subscriber));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void depriveSubscriber(EventSubscriber<?> subscriber) {
		checkSubscriber(subscriber, annotation -> events.remove(annotation.value(), (EventSubscriber<? super Event>) subscriber));
	}

	private void checkSubscriber(EventSubscriber<?> subscriber, Consumer<SubscribesTo> consumer) {
		Optional<SubscribesTo> optional = ClassUtils.getAnnotation(subscriber.getClass(), SubscribesTo.class);
		Preconditions.checkArgument(optional.isPresent(), String.format("%s is not annotated with @SubscribesTo", subscriber.getClass()));
		consumer.accept(optional.get());
	}

	@Override
	public <E extends Event> void post(Player player, E event) {
		Collection<EventSubscriber<? super Event>> subscribers = events.get(event.getClass());

		for (EventSubscriber<? super Event> subscriber : subscribers) {
			/* Check to be sure we can subscribe to the event. */
			if (subscriber.test(event)) {
				subscriber.subscribe(context, player, event);

				/* If the chain is broken, don't continue parsing subscribers. */
				if (context.isChainBroken()) {
					break;
				}
			}
		}

		context.repairSubscriberChain();
	}
	
	public Multimap<Class<? extends Event>, EventSubscriber<? super Event>> getEvents() {
		return events;
	}

}
