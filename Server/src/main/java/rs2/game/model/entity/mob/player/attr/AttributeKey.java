package rs2.game.model.entity.mob.player.attr;

import java.util.Objects;

/**
 * Represents a key for an {@link Attribute<T>}
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 *
 * @param <T> The attributes values type reference.
 */
public final class AttributeKey<T> {

	/**
	 * Represents the name of this attribute key. An attributes key is an
	 * identifier that encapsulates this attributes name. This key is used for
	 * representing an attribute through some collection.
	 */
	private final String name;

	/**
	 * Returns the initial value of the specified type reference.
	 */
	private final T initial;

	/**
	 * Constructs a new {@link AttributeKey} with the specified name and initial
	 * value.
	 *
	 * @param name The name of this attribute, may not be {@code null}.
	 * @param initial The initial value of the specified type reference.
	 *
	 *            <p>
	 *            This constructor should not be invoked directly, use
	 *            {@link #valueOf(String, Object)} instead!
	 *            </p>
	 */
	private AttributeKey(String name, T initial) {
		this.name = Objects.requireNonNull(name);
		this.initial = initial;
	}

	/**
	 * Returns this attribute keys name.
	 */
	protected String getName() {
		return name;
	}

	/**
	 * Returns the initial value of the specified type reference.
	 */
	protected T getInitial() {
		return initial;
	}

	/**
	 * Constructs a new {@link AttributeKey<T>} with the specified name and
	 * initial value.
	 *
	 * @param <T> The attributes values type reference.
	 * @param name The name of this attribute, may not be {@code null}.
	 * @param initial The initial value of the specified type reference.
	 * @see {@link AttributeKey(String, T)}
	 */
	public static <T> AttributeKey<T> valueOf(String name, T initial) {
		return new AttributeKey<>(name, initial);
	}

	/**
	 * Constructs a new {@link AttributeKey<T>} with the specified name and
	 * {@code null} for the initial value.
	 *
	 * @param <T> The attributes values type reference.
	 * @param name The name of this attribute, may not be {@code null}.
	 * @see {@link AttributeKey(String, T)}
	 */
	public static <T> AttributeKey<T> valueOf(String name) {
		return new AttributeKey<>(name, null);
	}

}
