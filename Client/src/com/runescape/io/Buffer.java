package com.runescape.io;

import java.math.BigInteger;

import com.runescape.Configuration;
import com.runescape.collection.Cacheable;
import com.runescape.net.IsaacCipher;
import com.runescape.sign.SignLink;

public final class Buffer extends Cacheable {
	
	public byte payload[];
	public int currentPosition;
	public int bitPosition;
	private static final int[] BIT_MASKS = { 0, 1, 3, 7, 15, 31, 63, 127, 255,
			511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff,
			0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
			0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
			0x7fffffff, -1 };
	
	public IsaacCipher encryption;

	public static Buffer create() {
		Buffer buffer = new Buffer();		
		buffer.currentPosition = 0;
		buffer.payload = new byte[5000];
		return buffer;
	}

	public final int readUTriByte() {
		currentPosition += 3;
		return (0xff & payload[currentPosition - 3] << 16)
				+ (0xff & payload[currentPosition - 2] << 8)
				+ (0xff & payload[currentPosition - 1]);
	}

	private Buffer() {}

	public Buffer(byte[] payload) {
		this.payload = payload;
		currentPosition = 0;
	}

	public int readUSmart2() {
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = readUSmart()) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public String readNewString() {
		int i = currentPosition;
		while (payload[currentPosition++] != 0)
			;
		return new String(payload, i, currentPosition - i - 1);
	}

	public void writeOpcode(int opcode) {		
		payload[currentPosition++] = (byte) (opcode + encryption.getNextKey());
	}

	public void writeByte(int value) {
		payload[currentPosition++] = (byte) value;
	}

	public void writeShort(int value) {
		payload[currentPosition++] = (byte) (value >> 8);
		payload[currentPosition++] = (byte) value;
	}

	public void writeTriByte(int value) {
		payload[currentPosition++] = (byte) (value >> 16);
		payload[currentPosition++] = (byte) (value >> 8);
		payload[currentPosition++] = (byte) value;
	}

	public void writeInt(int value) {
		payload[currentPosition++] = (byte) (value >> 24);
		payload[currentPosition++] = (byte) (value >> 16);
		payload[currentPosition++] = (byte) (value >> 8);
		payload[currentPosition++] = (byte) value;
	}

	public void writeLEInt(int value) {
		payload[currentPosition++] = (byte) value;
		payload[currentPosition++] = (byte) (value >> 8);
		payload[currentPosition++] = (byte) (value >> 16);
		payload[currentPosition++] = (byte) (value >> 24);
	}

	public void writeLong(long value) {
		try {
			payload[currentPosition++] = (byte) (int) (value >> 56);
			payload[currentPosition++] = (byte) (int) (value >> 48);
			payload[currentPosition++] = (byte) (int) (value >> 40);
			payload[currentPosition++] = (byte) (int) (value >> 32);
			payload[currentPosition++] = (byte) (int) (value >> 24);
			payload[currentPosition++] = (byte) (int) (value >> 16);
			payload[currentPosition++] = (byte) (int) (value >> 8);
			payload[currentPosition++] = (byte) (int) value;
		} catch (RuntimeException runtimeexception) {
			SignLink.reporterror("14395, " + 5 + ", " + value + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void writeString(String text) {
		System.arraycopy(text.getBytes(), 0, payload, currentPosition,
				text.length());
		currentPosition += text.length();
		payload[currentPosition++] = 10;
	}

	public void writeBytes(byte data[], int offset, int length) {
		for (int index = length; index < length + offset; index++)
			payload[currentPosition++] = data[index];
	}

	public void writeBytes(int value) {
		payload[currentPosition - value - 1] = (byte) value;
	}

	public int readUnsignedByte() {
		return payload[currentPosition++] & 0xff;
	}

	public byte readSignedByte() {
		return payload[currentPosition++];
	}

	public int readUShort() {
		currentPosition += 2;
		return ((payload[currentPosition - 2] & 0xff) << 8)
				+ (payload[currentPosition - 1] & 0xff);
	}

	public int readShort() {
		currentPosition += 2;
		int value = ((payload[currentPosition - 2] & 0xff) << 8)
				+ (payload[currentPosition - 1] & 0xff);
		
		if (value > 32767) {
			value -= 0x10000;
		}
		return value;
	}

	public int readTriByte() {
		currentPosition += 3;
		return ((payload[currentPosition - 3] & 0xff) << 16)
				+ ((payload[currentPosition - 2] & 0xff) << 8)
				+ (payload[currentPosition - 1] & 0xff);
	}

	public int readInt() {
		currentPosition += 4;
		return ((payload[currentPosition - 4] & 0xff) << 24)
				+ ((payload[currentPosition - 3] & 0xff) << 16)
				+ ((payload[currentPosition - 2] & 0xff) << 8)
				+ (payload[currentPosition - 1] & 0xff);
	}

	public long readLong() {
		long msi = (long) readInt() & 0xffffffffL;
		long lsi = (long) readInt() & 0xffffffffL;
		return (msi << 32) + lsi;
	}

	public String readString() {
		int index = currentPosition;
		while (payload[currentPosition++] != 10)
			;
		return new String(payload, index, currentPosition - index - 1);
	}

	public byte[] readBytes() {
		int index = currentPosition;
		while (payload[currentPosition++] != 10)
			;
		byte data[] = new byte[currentPosition - index - 1];
		System.arraycopy(payload, index, data, index - index, currentPosition - 1 - index);
		return data;
	}

	public void readBytes(int offset, int length, byte data[]) {
		for (int index = length; index < length + offset; index++)
			data[index] = payload[currentPosition++];
	}

	public void initBitAccess() {
		bitPosition = currentPosition * 8;
	}

	public int readBits(int amount) {
		int byteOffset = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		int value = 0;
		bitPosition += amount;
		for (; amount > bitOffset; bitOffset = 8) {
			value += (payload[byteOffset++] & BIT_MASKS[bitOffset]) << amount
					- bitOffset;
			amount -= bitOffset;
		}
		if (amount == bitOffset)
			value += payload[byteOffset] & BIT_MASKS[bitOffset];
		else
			value += payload[byteOffset] >> bitOffset - amount
					& BIT_MASKS[amount];
		return value;
	}

	public void disableBitAccess() {	      
		currentPosition = (bitPosition + 7) / 8;
	}

	public int readSmart() {
		int value = payload[currentPosition] & 0xff;
		if (value < 128)
			return readUnsignedByte() - 64;
		else
			return readUShort() - 49152;
	}

	public int readUSmart() {
		int value = payload[currentPosition] & 0xff;
		if (value < 128)
			return readUnsignedByte();
		else
			return readUShort() - 32768;
	}

	public void encodeRSA(BigInteger exponent, BigInteger modulus) {
		int length = currentPosition;
		currentPosition = 0;
		byte buffer[] = new byte[length];
		readBytes(length, 0, buffer);

		byte rsa[] = buffer;

		if (Configuration.ENABLE_RSA) {
			rsa = new BigInteger(buffer).modPow(exponent, modulus)
					.toByteArray();
		}

		currentPosition = 0;
		writeByte(rsa.length);
		writeBytes(rsa, rsa.length, 0);
	}

	public void writeNegatedByte(int value) {
		payload[currentPosition++] = (byte) (-value);
	}

	public void writeByteS(int value) {
		payload[currentPosition++] = (byte) (128 - value);
	}

	public int readUByteA() {
		return payload[currentPosition++] - 128 & 0xff;
	}

	public int readNegUByte() {
		return -payload[currentPosition++] & 0xff;
	}

	public int readUByteS() {
		return 128 - payload[currentPosition++] & 0xff;
	}

	public byte readNegByte() {
		return (byte) -payload[currentPosition++];
	}

	public byte readByteS() {
		return (byte) (128 - payload[currentPosition++]);
	}

	public void writeLEShort(int value) {
		payload[currentPosition++] = (byte) value;
		payload[currentPosition++] = (byte) (value >> 8);
	}

	public void writeShortA(int value) {
		payload[currentPosition++] = (byte) (value >> 8);
		payload[currentPosition++] = (byte) (value + 128);
	}

	public void writeLEShortA(int value) {
		payload[currentPosition++] = (byte) (value + 128);
		payload[currentPosition++] = (byte) (value >> 8);
	}

	public int readLEUShort() {
		currentPosition += 2;
		return ((payload[currentPosition - 1] & 0xff) << 8)
				+ (payload[currentPosition - 2] & 0xff);
	}

	public int readUShortA() {
		currentPosition += 2;
		return ((payload[currentPosition - 2] & 0xff) << 8)
				+ (payload[currentPosition - 1] - 128 & 0xff);
	}

	public int readLEUShortA() {
		currentPosition += 2;
		return ((payload[currentPosition - 1] & 0xff) << 8)
				+ (payload[currentPosition - 2] - 128 & 0xff);
	}

	public int readLEShort() {
		currentPosition += 2;
		int value = ((payload[currentPosition - 1] & 0xff) << 8)
				+ (payload[currentPosition - 2] & 0xff);
		
		if (value > 32767) {
			value -= 0x10000;
		}
		return value;
	}

	public int readLEShortA() {
		currentPosition += 2;
		int value = ((payload[currentPosition - 1] & 0xff) << 8)
				+ (payload[currentPosition - 2] - 128 & 0xff);
		if (value > 32767)
			value -= 0x10000;
		return value;
	}

	public int readMEInt() { // V1
		currentPosition += 4;
		return ((payload[currentPosition - 2] & 0xff) << 24)
				+ ((payload[currentPosition - 1] & 0xff) << 16)
				+ ((payload[currentPosition - 4] & 0xff) << 8)
				+ (payload[currentPosition - 3] & 0xff);
	}

	public int readIMEInt() { // V2
		currentPosition += 4;
		return ((payload[currentPosition - 3] & 0xff) << 24)
				+ ((payload[currentPosition - 4] & 0xff) << 16)
				+ ((payload[currentPosition - 1] & 0xff) << 8)
				+ (payload[currentPosition - 2] & 0xff);
	}

	public void writeReverseDataA(byte data[], int length, int offset) {
		for (int index = (length + offset) - 1; index >= length; index--) {
			payload[currentPosition++] = (byte) (data[index] + 128);
		}

	}

	public void readReverseData(byte data[], int offset, int length) {
		for (int index = (length + offset) - 1; index >= length; index--) {
			data[index] = payload[currentPosition++];
		}

	}
	
}