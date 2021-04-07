package com.runescape.sound;

import com.runescape.io.Buffer;

/**
 * an implementation of a reconfigurable filter that calculates
 * coefficients from pole magnitude/phases and a serial
 * configuration of cascading second-order iir filters
 * Refactored information from Major's 317 refactored client
 * http://www.rune-server.org/runescape-development/rs2-client/downloads/575183-almost-fully-refactored-317-client.html
 */
final class Filter {

	final int[] pairs;
	private final int[][][] phases;
	private final int[][][] magnitudes;
	private final int[] unity;
	private static final float[][] minimisedCoefficients = new float[2][8];
	static final int[][] coefficients = new int[2][8];
	private static float forwardMinimisedCoefficientMultiplier;
	static int forwardMultiplier;

	public Filter() {
		pairs = new int[2];
		phases = new int[2][2][4];
		magnitudes = new int[2][2][4];
		unity = new int[2];
	}

	/**
	 * Perform precise linear interpolation on the magnitude (where "precise" means that the result is guaranteed to be
	 * the first magnitude ({@code magnitudes[direction][1][pair]}) when the step is {@code 1}).
	 * 
	 * @param direction The direction, where {@code 0} is feedforward, and {@code 1} is feedback.
	 * @param pair The pair.
	 * @param step The step (the interpolation parameter).
	 * @return The interpolated magnitude.
	 */
	private float interpolateMagnitude(int direction, int pair, float step) {
		float magnitude = (float) magnitudes[direction][0][pair] + step
				* (float) (magnitudes[direction][1][pair] - magnitudes[direction][0][pair]);
		magnitude *= 0.001525879F;
		return 1.0F - (float) Math.pow(10D, -magnitude / 20F);
	}

	private float normalise(float exponent) {
		float f1 = 32.7032F * (float) Math.pow(2D, exponent);
		return (f1 * 3.141593F) / 11025F;
	}

	/**
	 * Perform linear interpolation on the phase
	 * 
	 * @param direction The direction, where {@code 0} is feedforward, and {@code 1} is feedback.
	 * @param pair The pair.
	 * @param step The step (the interpolation parameter).
	 * @return The interpolated phase.
	 */
	private float interpolatePhase(int direction, int pair, float step) {
		float phase = (float) phases[direction][0][pair] + step
				* (float) (phases[direction][1][pair] - phases[direction][0][pair]);
		phase *= 0.0001220703F;
		return normalise(phase);
	}

	public int compute(int direction, float step) {
		if (direction == 0) {
			float unity = (float) this.unity[0] + (float) (this.unity[1] - this.unity[0]) * step;
			unity *= 0.003051758F;
			forwardMinimisedCoefficientMultiplier = (float) Math.pow(
					0.10000000000000001D, unity / 20F);
			forwardMultiplier = (int) (forwardMinimisedCoefficientMultiplier * 65536F);
		}
		if (pairs[direction] == 0)
			return 0;
		float initialMagnitude = interpolateMagnitude(direction, 0, step);
		minimisedCoefficients[direction][0] = -2F * initialMagnitude
				* (float) Math.cos(interpolatePhase(direction, 0, step));
		minimisedCoefficients[direction][1] = initialMagnitude * initialMagnitude;
		for (int pair = 1; pair < pairs[direction]; pair++) {
			float magnitude = interpolateMagnitude(direction, pair, step);
			float f4 = -2F * magnitude * (float) Math.cos(interpolatePhase(direction, pair, step));
			float f5 = magnitude * magnitude;
			minimisedCoefficients[direction][pair * 2 + 1] = minimisedCoefficients[direction][pair * 2 - 1]
					* f5;
			minimisedCoefficients[direction][pair * 2] = minimisedCoefficients[direction][pair * 2 - 1]
					* f4 + minimisedCoefficients[direction][pair * 2 - 2] * f5;
			for (int j1 = pair * 2 - 1; j1 >= 2; j1--)
				minimisedCoefficients[direction][j1] += minimisedCoefficients[direction][j1 - 1]
						* f4 + minimisedCoefficients[direction][j1 - 2] * f5;

			minimisedCoefficients[direction][1] += minimisedCoefficients[direction][0] * f4
					+ f5;
			minimisedCoefficients[direction][0] += f4;
		}

		if (direction == 0) {
			for (int l = 0; l < pairs[0] * 2; l++)
				minimisedCoefficients[0][l] *= forwardMinimisedCoefficientMultiplier;

		}
		for (int pair = 0; pair < pairs[direction] * 2; pair++)
			coefficients[direction][pair] = (int) (minimisedCoefficients[direction][pair] * 65536F);

		return pairs[direction] * 2;
	}

	public void decode(Buffer stream, Envelope soundEnveleope) {
		int count = stream.readUnsignedByte();
		pairs[0] = count >> 4;
		pairs[1] = count & 0xf;
		if (count != 0) {
			unity[0] = stream.readUShort();
			unity[1] = stream.readUShort();
			int migration = stream.readUnsignedByte();
			for (int k = 0; k < 2; k++) {
				for (int l = 0; l < pairs[k]; l++) {
					phases[k][0][l] = stream.readUShort();
					magnitudes[k][0][l] = stream.readUShort();
				}

			}

			for (int direction = 0; direction < 2; direction++) {
				for (int pair = 0; pair < pairs[direction]; pair++)
					if ((migration & 1 << direction * 4 << pair) != 0) {
						phases[direction][1][pair] = stream
								.readUShort();
						magnitudes[direction][1][pair] = stream
								.readUShort();
					} else {
						phases[direction][1][pair] = phases[direction][0][pair];
						magnitudes[direction][1][pair] = magnitudes[direction][0][pair];
					}

			}

			if (migration != 0 || unity[1] != unity[0])
				soundEnveleope.decodeSegments(stream);
		} else {
			unity[0] = unity[1] = 0;
		}
	}
}
