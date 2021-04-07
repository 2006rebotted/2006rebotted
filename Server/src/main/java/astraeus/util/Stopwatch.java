package astraeus.util;

import java.util.concurrent.TimeUnit;

/**
 * A simple timing utility used to throttle or time actions. This class has been
 * altered to use <code>NANOSECONDS</code> for extremely accurate timing.
 * 
 * @author lare96
 */
public class Stopwatch {

	/**
	 * Gets the current time using <code>System.nanoTime()</code> and converts
	 * it to <code>MILLISECONDS</code>.
	 * 
	 * @return the current time, in milliseconds.
	 */
	public static long currentTime() {
		return TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	/** The internal cached time for this stopwatch. */
	private long time = Stopwatch.currentTime();

	/**
	 * Returns the amount of time elapsed since this object was initialized, or
	 * since the last call to the <code>reset()</code> method.
	 * 
	 * @return the elapsed time in <code>MILLISECONDS</code>.
	 */
	public long elapsed() {
		return Stopwatch.currentTime() - time;
	}

	/**
	 * Returns the amount of time elapsed since this object was initialized, or
	 * since the last call to the <code>reset()</code> method in
	 * <code>unit</code>.
	 * 
	 * @param unit
	 *            the time unit to convert the elapsed time into.
	 * 
	 * @return the elapsed time in <code>unit</code>.
	 */
	public long elapsed(TimeUnit unit) {
		if (unit == TimeUnit.MILLISECONDS) {
			throw new IllegalArgumentException("Time is already in milliseconds!");
		}
		return unit.convert(elapsed(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Resets the internal cached time to <tt>0</tt>.
	 * 
	 * @return the stopwatch instance.
	 */
	public Stopwatch reset() {
		time = Stopwatch.currentTime();
		return this;
	}

	/**
	 * Resets the internal cached time, but instead of resetting it to
	 * <tt>0</tt> it resets it to start at <code>start</code>.
	 * 
	 * @param start
	 *            the time to start this stopwatch at.
	 * @return the stopwatch instance.
	 */
	public Stopwatch reset(long start) {
		time = Stopwatch.currentTime() - start;
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("STOPWATCH[time=%s, elapsed=%s]", time, elapsed());
	}
}