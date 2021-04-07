package com.runescape.sound;

import com.runescape.io.Buffer;

/**
 * A simple envelope generator to control a variety of parameters (such as
 * attack and release).
 * Refactored using Major's 317 refactored client
 * http://www.rune-server.org/runescape-development/rs2-client/downloads/575183-almost-fully-refactored-317-client.html
 */
final class Envelope {

	private int segments;
	private int[] durations;
	private int[] peaks;
	int start;
	int end;
	int form;
	private int threshold;
	private int segmentIndex;
	private int step;
	private int amplitude;
	private int ticks;

	public void decode(Buffer stream) {
		form = stream.readUnsignedByte();
		start = stream.readInt();
		end = stream.readInt();
		decodeSegments(stream);
	}

	/**
	 * Decodes the segment data from the specified {@link Buffer}.
	 * 
	 * @param buffer
	 *            The buffer.
	 */
	public void decodeSegments(Buffer stream) {
		segments = stream.readUnsignedByte();
		durations = new int[segments];
		peaks = new int[segments];
		for (int i = 0; i < segments; i++) {
			durations[i] = stream.readUShort();
			peaks[i] = stream.readUShort();
		}

	}

	/**
	 * Resets this envelope.
	 */
	void resetValues() {
		threshold = 0;
		segmentIndex = 0;
		step = 0;
		amplitude = 0;
		ticks = 0;
	}

	/**
	 * Proceeds to the next step of the envelope,
	 * 
	 * @param period
	 *            The current period.
	 * @return The change.
	 */
	int step(int period) {
		if (ticks >= threshold) {
			amplitude = peaks[segmentIndex++] << 15;
			if (segmentIndex >= segments)
				segmentIndex = segments - 1;
			threshold = (int) (((double) durations[segmentIndex] / 65536D) * (double) period);
			if (threshold > ticks)
				step = ((peaks[segmentIndex] << 15) - amplitude)
						/ (threshold - ticks);
		}
		amplitude += step;
		ticks++;
		return amplitude - step >> 15;
	}
}
