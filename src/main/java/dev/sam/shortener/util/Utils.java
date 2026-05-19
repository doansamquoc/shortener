package dev.sam.shortener.util;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
	public static String generateCode() {
		int code = ThreadLocalRandom.current().nextInt(1000000);
		return String.valueOf(code);
	}
}
