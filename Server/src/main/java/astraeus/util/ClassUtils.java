package astraeus.util;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * A static-utility class containing extension or helper methods for
 * {@link Class}es or generic objects.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ClassUtils {

	/**
	 * Returns the specified {@link T annotation} for the potentially annotated
	 * {@link Class class} if it exists.
	 * 
	 * @param clazz The class to test contains the specified {@code annotation}.
	 * @param annotation The annotation to check the specified {@code class}
	 *            for.
	 * @return The {@code annotation} if and only if it exists otherwise
	 *         {@link Optional#empty()}.
	 */
	public static <T extends Annotation> Optional<T> getAnnotation(Class<?> clazz, Class<T> annotation) {
		return Optional.ofNullable(clazz.getAnnotation(annotation));
	}

	/**
	 * Suppresses the default-public constructor preventing this class from
	 * being instantiated by other classes, instantiating this class from within
	 * itself will throw an {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException If this class is instantiated from
	 *             within itself.
	 */
	private ClassUtils() {
		throw new UnsupportedOperationException("static-utility classes may not be instantiated.");
	}

}
