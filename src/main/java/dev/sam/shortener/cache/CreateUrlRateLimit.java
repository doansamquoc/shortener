package dev.sam.shortener.cache;

public interface CreateUrlRateLimit {
	void add(String key, long duration);

	boolean isLimited(String key);
}
