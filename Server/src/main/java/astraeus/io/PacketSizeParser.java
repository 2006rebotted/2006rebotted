package astraeus.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;

import astraeus.io.PacketSizeParser.PacketSize;
import astraeus.net.NetworkConstants;
import astraeus.util.GsonObjectParser;

public final class PacketSizeParser extends GsonObjectParser<PacketSize> {

	public PacketSizeParser() {
		super("./data/io/packet_sizes");
	}

	@Override
	public PacketSize[] deserialize(Gson gson, FileReader reader) throws IOException {
		return gson.fromJson(reader, PacketSize[].class);
	}

	@Override
	public void onRead(PacketSize[] array) throws IOException {
		Arrays.stream(array).forEach($it -> NetworkConstants.PACKET_SIZES[$it.getOpcode()] = $it.getSize());
	}
	
	public static final class PacketSize {
		
		private int opcode;
		
		private final int size;
		
		public PacketSize(int opcode, int size) {
			this.opcode = opcode;
			this.size = size;
		}

		public int getOpcode() {
			return opcode;
		}

		public int getSize() {
			return size;
		}	
		
	}

}
