package dev.sam.shortener.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
	// Get JWT from HttpServletRequest's header
	public static String getJwt(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) return null;
		return header.substring(7);
	}
}
