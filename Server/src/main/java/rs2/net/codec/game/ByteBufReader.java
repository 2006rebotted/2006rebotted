package rs2.net.codec.game;

import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Functions as modified read-only {@link ByteBuf} for the rs2 protocol.
 * 
 * @author Vult-R
 */
public final class ByteBufReader {

	/**
	 * The underlying buffer that is being read.
	 */
	private final ByteBuf buf;

	/**
	 * Creates a new {@link ByteBufReader}.
	 */
	public ByteBufReader(ByteBuf buf) {
		this.buf = buf;
	}
	
	public ByteBufReader(byte[] data) {
		this.buf = Unpooled.wrappedBuffer(data);
	}
	
	public static ByteBufReader wrap(byte[] data) {
		return new ByteBufReader(data);
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} byte from the payload.
	 *
	 * @return The byte.
	 */
	public int readByte() {
		return readByte(true, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code STANDARD} byte from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The byte.
	 */
	public int readByte(boolean signed) {
		return readByte(signed, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code signed} byte from the payload.
	 * 
	 * @param mod
	 *            The modification performed on this value.
	 * 
	 * @return The signed byte.
	 */
	public int readByte(ByteModification mod) {
		return readByte(true, mod);
	}

	/**
	 * Reads a single byte from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modification performed on this value.
	 * 
	 * @return The byte.
	 */
	public int readByte(boolean signed, ByteModification mod) {
		int value = buf.readByte();
		switch (mod) {
		case ADDITION:
			value = value + 128;
			break;

		case SUBTRACTION:
			value = 128 - value;
			break;

		case NEGATION:
			value = -value;
			break;

		case STANDARD:
			break;

		}
		return signed ? value : value & 0xFF;
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} short value from the payload in
	 * {@code BIG} order.
	 * 
	 * @return The standard signed short value.
	 */
	public int readShort() {
		return readShort(true, ByteOrder.BIG, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code STANDARD} short value from the payload in {@code BIG}
	 * order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The standard short value.
	 */
	public int readShort(boolean signed) {
		return readShort(signed, ByteOrder.BIG, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} short value from the payload.
	 * 
	 * @param order
	 *            The order in which this value is written.
	 * 
	 * @return The signed standard short value.
	 */
	public int readShort(ByteOrder order) {
		return readShort(true, order, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code STANDARD} short value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which this value is written.
	 * 
	 * @return The standard short value.
	 */
	public int readShort(boolean signed, ByteOrder order) {
		return readShort(signed, order, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code signed} short value from the payload.
	 * 
	 * @param order
	 *            The order in which this value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(ByteOrder order, ByteModification mod) {
		return readShort(true, order, mod);
	}

	/**
	 * Reads a {@code signed} short value from the payload {@code BIG} order.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(ByteModification mod) {
		return readShort(true, ByteOrder.BIG, mod);
	}

	/**
	 * Reads a short value from the payload in {@code BIG} order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(boolean signed, ByteModification mod) {
		return readShort(signed, ByteOrder.BIG, mod);
	}

	/**
	 * Reads a short value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(boolean signed, ByteOrder order, ByteModification mod) {
		int value = 0;
		switch (order) {
		case BIG:
			value |= readByte(false) << 8;
			value |= readByte(false, mod);
			break;

		case INVERSE_MIDDLE:
			throw new UnsupportedOperationException("Inverse-middle-endian short is impossible!");

		case MIDDLE:
			throw new UnsupportedOperationException("Middle-endian short " + "is impossible!");

		case LITTLE:
			value |= readByte(false, mod);
			value |= readByte(false) << 8;
			break;
		}
		return signed ? value : value & 0xFFFF;
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} integer value from the payload in
	 * {@code BIG} order.
	 *
	 * @return The integer value.
	 */
	public int readInt() {
		return readInt(true, ByteOrder.BIG, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code STANDARD} integer value from the payload in {@code BIG}
	 * order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed) {
		return readInt(signed, ByteOrder.BIG, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code signed} integer value from the payload in
	 * {@link ByteOrder} {@code BIG} order.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The integer value.
	 */
	public int readInt(ByteModification mod) {
		return readInt(true, ByteOrder.BIG, mod);
	}

	/**
	 * Reads an integer value from the payload in {@code BIG} order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed, ByteModification mod) {
		return readInt(signed, ByteOrder.BIG, mod);
	}

	/**
	 * Reads a {@code STANDARD} integer value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed, ByteOrder order) {
		return readInt(signed, order, ByteModification.STANDARD);
	}

	/**
	 * Reads an integer value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed, ByteOrder order, ByteModification mod) {
		long value = 0;
		switch (order) {
		case BIG:
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			value |= readByte(false) << 8;
			value |= readByte(false, mod);
			break;

		case MIDDLE:
			value |= readByte(false) << 8;
			value |= readByte(false, mod);
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			break;
		case INVERSE_MIDDLE:
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			value |= readByte(false, mod);
			value |= readByte(false) << 8;
			break;

		case LITTLE:
			value |= readByte(false, mod);
			value |= readByte(false) << 8;
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			break;

		}
		return (int) (signed ? value : value & 0xFFFFFFFFL);
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} long value from the payload in
	 * {@code BIG} order.
	 * 
	 * @return The long value.
	 */
	public long readLong() {
		return readLong(true, ByteOrder.BIG, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code STANDARD} long value from the payload in {@code BIG}
	 * order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed) {
		return readLong(signed, ByteOrder.BIG, ByteModification.STANDARD);
	}

	/**
	 * Reads a {@code signed} long value from the payload in {@code BIG} order.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The long value.
	 */
	public long readLong(ByteModification mod) {
		return readLong(true, ByteOrder.BIG, mod);
	}

	/**
	 * Reads a long value from the payload in {@code BIG} order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed, ByteModification mod) {
		return readLong(signed, ByteOrder.BIG, mod);
	}

	/**
	 * Reads a {@code STANDARD} long value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed, ByteOrder order) {
		return readLong(signed, order, ByteModification.STANDARD);
	}

	/**
	 * Reads a long value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed, ByteOrder order, ByteModification mod) {
		long value = 0;
		switch (order) {
		case BIG:
			value |= (long) readByte(false) << 56L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 8L;
			value |= readByte(false, mod);
			break;

		case MIDDLE:
		case INVERSE_MIDDLE:
			throw new UnsupportedOperationException("Middle and " + "inverse-middle value types not supported!");

		case LITTLE:
			value |= readByte(false, mod);
			value |= (long) readByte(false) << 8L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 56L;
			break;

		}
		return signed ? value : value & 0xFFFFFFFFL;
	}

	/**
	 * Reads a series of bytes from a buffer.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @return The bytes that where read.
	 */
	public byte[] readBytes(int amount) {
		return readBytes(amount, ByteModification.STANDARD);
	}

	/**
	 * Reads a series of bytes from a buffer.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @param mod
	 *            The modifications performed on the bytes values.
	 * 
	 * @return The bytes that where read.
	 */
	public byte[] readBytes(int amount, ByteModification mod) {
		byte[] data = new byte[amount];
		for (int i = 0; i < amount; i++) {
			data[i] = (byte) readByte(mod);
		}
		return data;
	}

	/**
	 * Reads a series of bytes in reverse.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @return The bytes in reverse.
	 */
	public byte[] readBytesReverse(int amount) {
		return readBytesReverse(amount, ByteModification.STANDARD);
	}

	/**
	 * Reads a series of bytes in reverse.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @param mod
	 *            The modification performed on these bytes.
	 * 
	 * @return The bytes in reverse.
	 */
	public byte[] readBytesReverse(int amount, ByteModification mod) {
		byte[] data = new byte[amount];

		int dataPosition = 0;

		for (int index = getPayload().readerIndex() + amount - 1; index >= getPayload().readerIndex(); index--) {
			int value = getPayload().getByte(index);

			switch (mod) {

			case ADDITION:
				value -= 128;
				break;

			case NEGATION:
				value = -value;
				break;

			case SUBTRACTION:
				value = 128 - value;
				break;

			case STANDARD:
				break;

			}
			data[dataPosition++] = (byte) value;
		}
		return data;
	}

	/**
	 * Reads a RuneScape string.
	 * 
	 * @return The string.
	 */
	public String getRS2String() {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.isReadable() && (b = buf.readByte()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}	
	
	/**
	 * Sets the position of this buffer.
	 * 
	 * @param position
	 * 		The position to set
	 */
	public void setPosition(int position) {
		buf.readerIndex(position);
	}
	
	/**
	 * Skips a specified number of bytes
	 * 
	 * @param amount
	 * 		The amount of bytes to skip.
	 */
	public void skipBytes(int amount) {
		this.buf.skipBytes(amount);
	}

	/**
	 * Gets the data in this buffer.
	 */
	public ByteBuf getPayload() {
		return buf;
	}

}
