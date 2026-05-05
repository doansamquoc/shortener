package dev.sam.shortener.util;

import static dev.sam.shortener.constant.AppConstant.ALPHABET;

public class Base62Encoder {
	private static final int BASE = ALPHABET.length();

	public static String encode(long input) {
		StringBuilder sb = new StringBuilder();
		while (input > 0) {
			sb.append(ALPHABET.charAt((int) (input % BASE)));
			input /= BASE;
		}
		return sb.reverse().toString();
	}

	public static long decode(String input) {
		long num = 0;
		for (int i = 0; i < input.length(); i++) {
			num = num * BASE + ALPHABET.indexOf(input.charAt(i));
		}
		return num;
	}
}
