package dev.sam.shortener.cache;

import java.util.concurrent.TimeUnit;

public interface TokenBlacklist {
	void add(String tokenId, long timeout, TimeUnit unit);

	boolean isBlacklisted(String tokenId);
}
