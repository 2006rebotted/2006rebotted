package com.runescape.util;

import com.runescape.io.Buffer;

public final class ChatMessageCodec {

	private static char[] message = new char[100];
	private static Buffer stream = new Buffer(new byte[100]);
	private static final char[] VALID_CHARACTERS = { ' ', 'e', 't', 'a', 'o', 'i',
			'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g',
			'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(',
			')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$',
			'%', '"', '[', ']' };

	public static String decode(int length, Buffer buffer) {
		int index = 0;
		int next = -1;
		for (int count = 0; count < length; count++) {
			int in = buffer.readUnsignedByte();
			int charIndex = in >> 4 & 0xf;
			if (next == -1) {
				if (charIndex < 13)
					message[index++] = VALID_CHARACTERS[charIndex];
				else
					next = charIndex;
			} else {
				message[index++] = VALID_CHARACTERS[((next << 4) + charIndex) - 195];
				next = -1;
			}
			charIndex = in & 0xf;
			if (next == -1) {
				if (charIndex < 13)
					message[index++] = VALID_CHARACTERS[charIndex];
				else
					next = charIndex;
			} else {
				message[index++] = VALID_CHARACTERS[((next << 4) + charIndex) - 195];
				next = -1;
			}
		}

		boolean capitaliseNext = true;
		for (int count = 0; count < index; count++) {
			char character = message[count];
			if (capitaliseNext && character >= 'a' && character <= 'z') {
				message[count] += '\uFFE0';
				capitaliseNext = false;
			}
			if (character == '.' || character == '!' || character == '?')
				capitaliseNext = true;
		}
		return new String(message, 0, index);
	}

	public static void encode(String string, Buffer buffer) {
		if (string.length() > 80)
			string = string.substring(0, 80);
		string = string.toLowerCase();
		int next = -1;
		for (int index = 0; index < string.length(); index++) {
			char character = string.charAt(index);
			int charIndex = 0;
			for (int count = 0; count < VALID_CHARACTERS.length; count++) {
				if (character != VALID_CHARACTERS[count])
					continue;
				charIndex = count;
				break;
			}
			
			if (charIndex > 12) {
				charIndex += 195;
			}
			if (next == -1) {
				if (charIndex < 13)
					next = charIndex;
				else
					buffer.writeByte(charIndex);
			} else if (charIndex < 13) {
				buffer.writeByte((next << 4) + charIndex);
				next = -1;
			} else {
				buffer.writeByte((next << 4) + (charIndex >> 4));
				next = charIndex & 0xf;
			}
		}
		if (next != -1) {
			buffer.writeByte(next << 4);
		}
	}

	public static String processText(String string) {
		stream.currentPosition = 0;
		encode(string, stream);
		int length = stream.currentPosition;
		stream.currentPosition = 0;
		
		return decode(length, stream);
	}
}
