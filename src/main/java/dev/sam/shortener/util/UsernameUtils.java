package dev.sam.shortener.util;

import java.util.Random;

public class UsernameUtils {
	public static String generateUsername(String email) {
		Random random = new Random();
		int randomInt = random.nextInt(10000);
		String rawUsername = email.split("@")[0].replace(".", "").replace("-", "").toLowerCase();
		return rawUsername + "_" + randomInt;
	}
}
