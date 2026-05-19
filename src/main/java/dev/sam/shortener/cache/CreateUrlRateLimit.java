package dev.sam.shortener.cache;

import java.time.Duration;

public interface CreateUrlRateLimit {
	void add(String key, Duration duration);

	boolean isLimited(String key);
}
