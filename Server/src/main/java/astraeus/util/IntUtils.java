package astraeus.util;

/**
 * The class that contains useful methods of {@link Integer} type.
 *
 * @author Seven
 */
public final class IntUtils {

	/**
	 * Gets an available index from a generic array.
	 * 
	 * @param array
	 *            The generic array.
	 * 
	 * @return -1 If not index was available, otherwise index;
	 */
	public static <T> int findFreeIndex(T[] array) {
		for (int index = 0; index < array.length; index++) {
			if (array[index] == null) {
				return index;
			}
		}
		return -1;
	}

}
