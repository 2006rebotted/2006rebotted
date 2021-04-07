package astraeus.net.codec;

import io.netty.buffer.ByteBuf;

/**
 * The class that contains useful ByteBuf methods for the rs2 protocol.
 * 
 * @author Vult-R
 */
public final class ByteBufUtils {

	private ByteBufUtils() {

	}

	/**
	 * Reads a RuneScape {@code String} value.
	 *
	 * @return the value of the string.
	 */
	public static String readJagString(ByteBuf buf) {
		byte temp;
		StringBuilder builder = new StringBuilder();
		while (buf.isReadable() && (temp = buf.readByte()) != 10) {
			builder.append((char) temp);
		}
		return builder.toString();
	}

	/**
	 * Reads a 'smart' (either a {@code byte} or {@code short} depending on the value) from the specified buffer.
	 *
	 * @param buffer The buffer.
	 * @return The 'smart'.
	 */
	public static int getUSmart(ByteBuf buffer) {
		// Reads a single byte from the buffer without modifying the current position.
		int peek = buffer.getByte(buffer.readerIndex()) & 0xFF;
		int value = peek > Byte.MAX_VALUE ? (buffer.readUnsignedShort()) + Short.MIN_VALUE : buffer.readUnsignedByte();
		return value;
	}

}
