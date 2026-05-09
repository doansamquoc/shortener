package dev.sam.shortener.cache;

public interface ResetTokenCache {
	void add(String token, String email, long duration);

	String get(String token);

	void remove(String token);
}
