package com.runescape.sound;

import com.runescape.io.Buffer;

/**
 * Refactored reference:
 * http://www.rune-server.org/runescape-development/rs2-client/downloads/575183-almost-fully-refactored-317-client.html
 */
public final class Track {

	private Track() {
		synthesizers = new Synthesizer[10];
	}
	
	public static void unpack(Buffer stream) {
		output = new byte[0x6baa8];
		riff = new Buffer(output);
		Synthesizer.init();
		do {
			int id = stream.readUShort();
			if (id == 65535)
				return;
			tracks[id] = new Track();
			tracks[id].decode(stream);
			delays[id] = tracks[id].calculateDelay();		
		} while (true);
	}

	public static Buffer data(int loops, int id) {
		if (tracks[id] != null) {
			Track soundTrack = tracks[id];
			return soundTrack.pack(loops);
		} else {
			return null;
		}
	}

	private void decode(Buffer stream) {
		for (int synthesizer = 0; synthesizer < 10; synthesizer++) {
			int valid = stream.readUnsignedByte();
			if (valid != 0) {
				stream.currentPosition--;
				synthesizers[synthesizer] = new Synthesizer();
				synthesizers[synthesizer].decode(stream);
			}
		}
		loopStart = stream.readUShort();
		loopEnd = stream.readUShort();
	}

	private int calculateDelay() {
		int offset = 0x98967f;
		for (int syntheziser = 0; syntheziser < 10; syntheziser++)
			if (synthesizers[syntheziser] != null
					&& synthesizers[syntheziser].offset / 20 < offset)
				offset = synthesizers[syntheziser].offset / 20;

		if (loopStart < loopEnd && loopStart / 20 < offset)
			offset = loopStart / 20;
		if (offset == 0x98967f || offset == 0)
			return 0;
		for (int synthesizer = 0; synthesizer < 10; synthesizer++)
			if (synthesizers[synthesizer] != null)
				synthesizers[synthesizer].offset -= offset * 20;

		if (loopStart < loopEnd) {
			loopStart -= offset * 20;
			loopEnd -= offset * 20;
		}
		return offset;
	}

	private Buffer pack(int loops) {
		int size = mix(loops);
		riff.currentPosition = 0;
		riff.writeInt(0x52494646);
		riff.writeLEInt(36 + size);
		riff.writeInt(0x57415645);
		riff.writeInt(0x666d7420);
		riff.writeLEInt(16);
		riff.writeLEShort(1);
		riff.writeLEShort(1);
		riff.writeLEInt(22050);
		riff.writeLEInt(22050);
		riff.writeLEShort(1);
		riff.writeLEShort(8);
		riff.writeInt(0x64617461);
		riff.writeLEInt(size);
		riff.currentPosition += size;
		return riff;
	}

	private int mix(int loops) {
		int duration = 0;
		for (int synthesizer = 0; synthesizer < 10; synthesizer++)
			if (synthesizers[synthesizer] != null
					&& synthesizers[synthesizer].duration + synthesizers[synthesizer].offset > duration)
				duration = synthesizers[synthesizer].duration + synthesizers[synthesizer].offset;

		if (duration == 0)
			return 0;
		int sampleCount = (22050 * duration) / 1000;
		int loopStart = (22050 * this.loopStart) / 1000;
		int loopEnd = (22050 * this.loopEnd) / 1000;
		if (loopStart < 0 || loopStart > sampleCount || loopEnd < 0 || loopEnd > sampleCount || loopStart >= loopEnd)
			loops = 0;
		int size = sampleCount + (loopEnd - loopStart) * (loops - 1);
		for (int offset = 44; offset < size + 44; offset++)
			output[offset] = -128;

		for (int synthesizer = 0; synthesizer < 10; synthesizer++)
			if (synthesizers[synthesizer] != null) {
				int synthDuration = (synthesizers[synthesizer].duration * 22050) / 1000;
				int synthOffset = (synthesizers[synthesizer].offset * 22050) / 1000;
				int samples[] = synthesizers[synthesizer].synthesize(synthDuration,
						synthesizers[synthesizer].duration);
				for (int sample = 0; sample < synthDuration; sample++)
					output[sample + synthOffset + 44] += (byte) (samples[sample] >> 8);

			}

		if (loops > 1) {
			loopStart += 44;
			loopEnd += 44;
			sampleCount += 44;
			int k2 = (size += 44) - sampleCount;
			for (int j3 = sampleCount - 1; j3 >= loopEnd; j3--)
				output[j3 + k2] = output[j3];

			for (int k3 = 1; k3 < loops; k3++) {
				int l2 = (loopEnd - loopStart) * k3;
				System.arraycopy(output, loopStart, output, loopStart + l2, loopEnd - loopStart);

			}

			size -= 44;
		}
		return size;
	}

	private static final Track[] tracks = new Track[5000];
	public static final int[] delays = new int[5000];
	private static byte[] output;
	private static Buffer riff;
	private final Synthesizer[] synthesizers;
	private int loopStart;
	private int loopEnd;

}
