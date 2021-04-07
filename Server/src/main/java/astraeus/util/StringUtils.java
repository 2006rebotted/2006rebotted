package astraeus.util;

import astraeus.Server;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StringUtils {
	
	private StringUtils() {
		
	}

	/**
	 * An array containing valid characters that can be used in the server.
	 */
	public static final char VALID_CHARACTERS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<',
			',', '"', '[', ']', '|', '?', '/', '`' };

	/**
	 * An array containing valid username characters.
	 */
	public static final char VALID_PLAYER_CHARACTERS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '[', ']', '/', '-', ' ' };
	
	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	/**
	 * Gets the elapsed time the server has been running for.
	 * 
	 * @return The elapsed time.
	 */
	public static String getUptime() {
		long elapsedSeconds = Server.getUptime().elapsed(TimeUnit.SECONDS);

		long minute = elapsedSeconds >= 60 ? elapsedSeconds / 60 : 0;

		long seconds = (Server.getUptime().elapsed(TimeUnit.SECONDS) >= 60
				? Server.getUptime().elapsed(TimeUnit.SECONDS) - (minute * 60)
				: Server.getUptime().elapsed(TimeUnit.SECONDS));

		long hour = minute >= 60 ? minute / 60 : 0;
		long day = hour >= 24 ? hour / 24 : 0;

		return String.format("[uptime= %d Days %d Hours %d Minutes %d Seconds]", day, hour, minute, seconds);
	}

	/**
	 * Formats a username
	 * 
	 * E.g Mod Seven
	 * 
	 * @param string
	 *            The username to modify.
	 *
	 * @return The formatted string.
	 */
	public static String formatName(final String string) {
		return Stream.of(string.trim().split("\\s")).filter(word -> word.length() > 0)
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
	}

	public static String formatPrice(int amount) {
		if (amount >= 1_000 && amount < 1_000_000) {
			return (amount / 1_000) + "K";
		}
		if (amount >= 1_000_000) {
			return (amount / 1_000_000) + "M";
		}
		if (amount >= 1_000_000_000) {
			return (amount / 1_000_000_000) + "B";
		}
		return "" + amount;
	}

	public static String formatValue(int value) {
		return new DecimalFormat("#, ###").format(value);
	}

	public static String getAOrAn(String nextWord) {
		String s = "a";
		final char c = nextWord.toUpperCase().charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
			s = "an";
		}
		return s;
	}

	public static String longToString(long l) {
		int i = 0;
		final char ac[] = new char[12];

		while (l != 0L) {
			final long l1 = l;

			l /= 37L;
			ac[11 - i++] = VALID_CHARACTERS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

}
