package com.runescape.sound;

import com.runescape.io.Buffer;

/**
 * Refactored reference:
 * http://www.rune-server.org/runescape-development/rs2-client/downloads/575183-almost-fully-refactored-317-client.html
 */
final class Synthesizer {

	private Envelope pitch;
	private Envelope volume;
	private Envelope pitchModifier;
	private Envelope pitchModifierAmplitude;
	private Envelope volumeMultiplier;
	private Envelope volumeMultiplierAmplitude;
	private Envelope release;
	private Envelope attack;
	private final int[] oscillatorVolume;
	private final int[] anIntArray107;
	private final int[] anIntArray108;
	private int delayTime;
	private int delayDecay;
	private Filter filter;
	private Envelope filterEnvelope;
	int duration;
	int offset;
	private static int[] samples;
	private static int[] NOISE;
	private static int[] SINE;
	private static final int[] phases = new int[5];
	private static final int[] delays = new int[5];
	private static final int[] volumeSteps = new int[5];
	private static final int[] pitchSteps = new int[5];
	private static final int[] pitchBaseSteps = new int[5];

	public Synthesizer() {
		oscillatorVolume = new int[5];
		anIntArray107 = new int[5];
		anIntArray108 = new int[5];
		delayDecay = 100;
		duration = 500;
	}

	public static void init() {
		NOISE = new int[32768];
		for (int i = 0; i < 32768; i++)
			if (Math.random() > 0.5D)
				NOISE[i] = 1;
			else
				NOISE[i] = -1;

		SINE = new int[32768];
		for (int j = 0; j < 32768; j++)
			SINE[j] = (int) (Math.sin((double) j / 5215.1903000000002D) * 16384D);

		samples = new int[0x35d54];
	}

	public int[] synthesize(int sampleCount, int duration) {
		for (int k = 0; k < sampleCount; k++)
			samples[k] = 0;

		if (duration < 10)
			return samples;
		double d = (double) sampleCount / ((double) duration + 0.0D);
		pitch.resetValues();
		volume.resetValues();
		int pitchMultiplierStep = 0;
		int pitchModifierBaseStep = 0;
		int pitchModifierPhase = 0;
		if (pitchModifier != null) {
			pitchModifier.resetValues();
			pitchModifierAmplitude.resetValues();
			pitchMultiplierStep = (int) (((double) (pitchModifier.end - pitchModifier.start) * 32.768000000000001D) / d);
			pitchModifierBaseStep = (int) (((double) pitchModifier.start * 32.768000000000001D) / d);
		}
		int volumeMultiplierStep = 0;
		int volumeMultiplierBaseStep = 0;
		int volumeMultiplierPhase = 0;
		if (volumeMultiplier != null) {
			volumeMultiplier.resetValues();
			volumeMultiplierAmplitude.resetValues();
			volumeMultiplierStep = (int) (((double) (volumeMultiplier.end - volumeMultiplier.start) * 32.768000000000001D) / d);
			volumeMultiplierBaseStep = (int) (((double) volumeMultiplier.start * 32.768000000000001D) / d);
		}
		for (int index = 0; index < 5; index++)
			if (oscillatorVolume[index] != 0) {
				phases[index] = 0;
				delays[index] = (int) ((double) anIntArray108[index] * d);
				volumeSteps[index] = (oscillatorVolume[index] << 14) / 100;
				pitchSteps[index] = (int) (((double) (pitch.end - pitch.start) * 32.768000000000001D * Math
						.pow(1.0057929410678534D, anIntArray107[index])) / d);
				pitchBaseSteps[index] = (int) (((double) pitch.start * 32.768000000000001D) / d);
			}

		for (int sample = 0; sample < sampleCount; sample++) {
			int pitchChange = pitch.step(sampleCount);
			int volumeChange = volume.step(sampleCount);
			if (pitchModifier != null) {
				int modifier = pitchModifier.step(sampleCount);
				int ampModifier = pitchModifierAmplitude.step(sampleCount);
				pitchChange += evaluateWave(ampModifier, pitchModifierPhase,
						pitchModifier.form) >> 1;
				pitchModifierPhase += (modifier * pitchMultiplierStep >> 16)
						+ pitchModifierBaseStep;
			}
			if (volumeMultiplier != null) {
				int multiplier = volumeMultiplier.step(sampleCount);
				int ampMultiplier = volumeMultiplierAmplitude.step(sampleCount);
				volumeChange = volumeChange
						* ((evaluateWave(ampMultiplier, volumeMultiplierPhase,
								volumeMultiplier.form) >> 1) + 32768) >> 15;
				volumeMultiplierPhase += (multiplier * volumeMultiplierStep >> 16)
						+ volumeMultiplierBaseStep;
			}
			for (int delay = 0; delay < 5; delay++)
				if (oscillatorVolume[delay] != 0) {
					int id = sample + delays[delay];
					if (id < sampleCount) {
						samples[id] += evaluateWave(volumeChange
								* volumeSteps[delay] >> 15, phases[delay],
								pitch.form);
						phases[delay] += (pitchChange * pitchSteps[delay] >> 16)
								+ pitchBaseSteps[delay];
					}
				}

		}

		if (release != null) {
			release.resetValues();
			attack.resetValues();
			int counter = 0;
			boolean muted = true;
			for (int sample = 0; sample < sampleCount; sample++) {
				int on = release.step(sampleCount);
				int off = attack.step(sampleCount);
				int threshold;
				if (muted)
					threshold = release.start
							+ ((release.end - release.start) * on >> 8);
				else
					threshold = release.start
							+ ((release.end - release.start) * off >> 8);
				if ((counter += 256) >= threshold) {
					counter = 0;
					muted = !muted;
				}
				if (muted)
					samples[sample] = 0;
			}

		}
		if (delayTime > 0 && delayDecay > 0) {
			int delay = (int) ((double) delayTime * d);
			for (int index = delay; index < sampleCount; index++)
				samples[index] += (samples[index - delay] * delayDecay) / 100;

		}
		if (filter.pairs[0] > 0 || filter.pairs[1] > 0) {
			filterEnvelope.resetValues();
			int change = filterEnvelope.step(sampleCount + 1);
			int forwardOrder = filter.compute(0, (float) change / 65536F);
			int backOrder = filter.compute(1, (float) change / 65536F);
			if (sampleCount >= forwardOrder + backOrder) {
				int index = 0;
				int delay = backOrder;
				if (delay > sampleCount - forwardOrder)
					delay = sampleCount - forwardOrder;
				for (; index < delay; index++) {
					int sample = (int) ((long) samples[index + forwardOrder]
							* (long) Filter.forwardMultiplier >> 16);
					for (int j8 = 0; j8 < forwardOrder; j8++)
						sample += (int) ((long) samples[(index + forwardOrder)
								- 1 - j8]
								* (long) Filter.coefficients[0][j8] >> 16);

					for (int j9 = 0; j9 < index; j9++)
						sample -= (int) ((long) samples[index - 1 - j9]
								* (long) Filter.coefficients[1][j9] >> 16);

					samples[index] = sample;
					change = filterEnvelope.step(sampleCount + 1);
				}

				char c = '\200';
				delay = c;
				do {
					if (delay > sampleCount - forwardOrder)
						delay = sampleCount - forwardOrder;
					for (; index < delay; index++) {
						int l8 = (int) ((long) samples[index + forwardOrder]
								* (long) Filter.forwardMultiplier >> 16);
						for (int k9 = 0; k9 < forwardOrder; k9++)
							l8 += (int) ((long) samples[(index + forwardOrder)
									- 1 - k9]
									* (long) Filter.coefficients[0][k9] >> 16);

						for (int i10 = 0; i10 < backOrder; i10++)
							l8 -= (int) ((long) samples[index - 1 - i10]
									* (long) Filter.coefficients[1][i10] >> 16);

						samples[index] = l8;
						change = filterEnvelope.step(sampleCount + 1);
					}

					if (index >= sampleCount - forwardOrder)
						break;
					forwardOrder = filter.compute(0, (float) change / 65536F);
					backOrder = filter.compute(1, (float) change / 65536F);
					delay += c;
				} while (true);
				for (; index < sampleCount; index++) {
					int sample = 0;
					for (int l9 = (index + forwardOrder) - sampleCount; l9 < forwardOrder; l9++)
						sample += (int) ((long) samples[(index + forwardOrder)
								- 1 - l9]
								* (long) Filter.coefficients[0][l9] >> 16);

					for (int j10 = 0; j10 < backOrder; j10++)
						sample -= (int) ((long) samples[index - 1 - j10]
								* (long) Filter.coefficients[1][j10] >> 16);

					samples[index] = sample;
				}

			}
		}
		for (int sample = 0; sample < sampleCount; sample++) {
			if (samples[sample] < -32768)
				samples[sample] = -32768;
			if (samples[sample] > 32767)
				samples[sample] = 32767;
		}

		return samples;
	}

	private int evaluateWave(int amplitude, int phase, int table) {
		if (table == 1)
			if ((phase & 0x7fff) < 16384)
				return amplitude;
			else
				return -amplitude;
		if (table == 2)
			return SINE[phase & 0x7fff] * amplitude >> 14;
		if (table == 3)
			return ((phase & 0x7fff) * amplitude >> 14) - amplitude;
		if (table == 4)
			return NOISE[phase / 2607 & 0x7fff] * amplitude;
		else
			return 0;
	}

	public void decode(Buffer stream) {
		pitch = new Envelope();
		pitch.decode(stream);
		volume = new Envelope();
		volume.decode(stream);
		int option = stream.readUnsignedByte();
		if (option != 0) {
			stream.currentPosition--;
			pitchModifier = new Envelope();
			pitchModifier.decode(stream);
			pitchModifierAmplitude = new Envelope();
			pitchModifierAmplitude.decode(stream);
		}
		option = stream.readUnsignedByte();
		if (option != 0) {
			stream.currentPosition--;
			volumeMultiplier = new Envelope();
			volumeMultiplier.decode(stream);
			volumeMultiplierAmplitude = new Envelope();
			volumeMultiplierAmplitude.decode(stream);
		}
		option = stream.readUnsignedByte();
		if (option != 0) {
			stream.currentPosition--;
			release = new Envelope();
			release.decode(stream);
			attack = new Envelope();
			attack.decode(stream);
		}
		for (int index = 0; index < 10; index++) {
			int volume = stream.readUSmart();
			if (volume == 0)
				break;
			oscillatorVolume[index] = volume;
			anIntArray107[index] = stream.readSmart();
			anIntArray108[index] = stream.readUSmart();
		}

		delayTime = stream.readUSmart();
		delayDecay = stream.readUSmart();
		duration = stream.readUShort();
		offset = stream.readUShort();
		filter = new Filter();
		filterEnvelope = new Envelope();
		filter.decode(stream, filterEnvelope);
	}

}
