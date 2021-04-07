package rs2.util;

import java.util.Random;

/**
 * The class that contains useful methods for generating random numbers.
 *
 * @author Seven
 */
public final class RandomUtils {

    /**
     * Random instance, used to generate pseudo-random primitive types.
     */
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * The private constructor to prevent instantiation.
     */
    private RandomUtils() {

    }

    /**
     * Generates an inclusive pseudo-random number with (approximately) equal probability. This
     * random number has a minimum value of {@code 0} and a maximum of {@code max}.
     *
     * @param max The maximum value.
     *
     * @return The random generated number.
     */
    public static int random(int max) {
        return random(0, max);
    }

    /**
     * Generates a pseudo-random number with (approximately) equal probability. This random number
     * has a minimum value of {@code 0} and a maximum of {@code max}.
     *
     * @param max The maximum value.
     *
     * @param inclusive The flag that denotes this number is inclusive.
     *
     * @return The random generated number.
     */
    public static int random(int max, boolean inclusive) {
        return random(0, max, inclusive);
    }

    /**
     * Generates an inclusive pseudo-random number with (approximately) equal probability.
     *
     * @param min The minimum value.
     *
     * @param max The maximum value.
     *
     * @return The random generated number.
     */
    public static int random(int min, int max) {
        return random(min, max, true);
    }

    /**
     * Generates a pseudo-random number with (approximately) equal probability
     *
     * @param min The minimum value.
     *
     * @param max The maximum value.
     *
     * @param inclusive The flag that denotes this value is inclusive.
     *
     * @return The random generated number.
     */
    public static int random(int min, int max, boolean inclusive) {
        return RANDOM.nextInt(max - min + (inclusive ? 1 : 0)) + min;
    }

    /**
     * Picks a random element out of any array type.
     *
     * @param array the array to pick the element from.
     * @return the element chosen.
     */
    public static int random(int[] array) {
        return array[(int) (RANDOM.nextDouble() * array.length)];
    }

}
