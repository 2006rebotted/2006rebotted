package rs2.game.event;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that types annotated are an {@link EventSubscriber} and subscribe
 * to one specific {@link Event}. {@link #value()} enforces that the specified
 * event value is indeed an event. All event subscribers MUST be annotated
 * otherwise {@link EventProvider}s will be unable to provide and deprive
 * subscribers.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface SubscribesTo {

	/**
	 * Returns the event class that the annotated {@link EventSubscriber}
	 * subscribes to.
	 */
	Class<? extends Event> value();

}
