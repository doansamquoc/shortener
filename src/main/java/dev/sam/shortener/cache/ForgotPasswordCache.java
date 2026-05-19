package dev.sam.shortener.cache;

public interface ForgotPasswordCache {
	void add(String email, String code, long duration);

	String get(String email);

	void remove(String email);
}
