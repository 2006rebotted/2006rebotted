package rs2.game.model.entity.mob.player.attr;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a map of {@link AttributeKey<T>}s to {@link Attribute<T>}s.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AttributeMap {

	/**
	 * A map of {@link AttributeKey}s to {@link Attribute}s.
	 */
	private final Map<AttributeKey<?>, Attribute<?>> attrs = new HashMap<>();

	/**
	 * Returns the value of the attribute which is represented by the specified
	 * {@link AttributeKey}, if no key exists the initial value is returned and
	 * the key is added.
	 *
	 * @param <T> The attributes value type reference.
	 * @param key The attribute key, may not be {@code null}.
	 * @return The value of the attribute.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(AttributeKey<T> key) {
		if (contains(key)) {
			Attribute<T> attr = (Attribute<T>) attrs.get(key);
			return attr.getValue();
		}

		return setAndGet(key, key.getInitial());
	}

	/**
	 * Sets a specified attribute key to a specified value and returns the
	 * value.
	 *
	 * @param <T> The attributes value type reference.
	 * @param key The attribute key, may not be {@code null}.
	 * @param value The value of the attribute.
	 * @return The value of the attribute.
	 */
	public <T> T setAndGet(AttributeKey<T> key, T value) {
		put(key, value);
		return value;
	}

	/**
	 * Sets a specified attribute key to a specified value.
	 *
	 * @param <T> The attributes value type reference.
	 * @param key The attribute key, may not be {@code null}.
	 * @param value The value of the attribute.
	 */
	public <T> void put(AttributeKey<T> key, T value) {
		attrs.put(key, new Attribute<>(key, value));
	}
	
	@SuppressWarnings("unchecked")
	public void toggle(AttributeKey<Boolean> key) {
		if (contains(key)) {
			Attribute<Boolean> attr = (Attribute<Boolean>) attrs.get(key);
			put(attr.getKey(), !attr.getValue());
		}
	}

	/**
	 * Returns a flag which denotes whether or not an attribute key exists
	 * within the {@link #attrs} map.
	 *
	 * @param <T> The attributes value type reference.
	 * @param key The attribute key, may not be {@code null}.
	 * @return {@code true} if and only if the specified key exists within the
	 *         attribute map, otherwise {@code false}.
	 */
	public <T> boolean contains(AttributeKey<T> key) {
		return attrs.containsKey(key);
	}

}
