package dev.sam.shortener.cache;

public interface MailRateLimit {
	void add(String email, long duration);

	boolean isLimited(String email);

	void decrease(String email);
}
