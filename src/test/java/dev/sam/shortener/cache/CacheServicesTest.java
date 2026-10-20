package dev.sam.shortener.cache;

import dev.sam.shortener.cache.impl.AuthTemporaryCodeImpl;
import dev.sam.shortener.cache.impl.CreateUrlRateLimitImpl;
import dev.sam.shortener.cache.impl.ForgotPasswordCacheImpl;
import dev.sam.shortener.cache.impl.MailRateLimitImpl;
import dev.sam.shortener.cache.impl.ResetTokenCacheImpl;
import dev.sam.shortener.cache.impl.TokenBlacklistImpl;
import dev.sam.shortener.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CacheServicesTest {

    private AppProperties props;

    @BeforeEach
    void setUp() {
        props = new AppProperties();
        props.setAuthCodeExpiration(100); // 100ms
        props.setCreateUrlLimit(2);
        props.getEmail().setLimitPerAccount(3);
    }

    @Test
    void testAuthTemporaryCode() throws InterruptedException {
        AuthTemporaryCodeImpl cache = new AuthTemporaryCodeImpl(props);
        cache.add("code-123", "user-payload");

        assertEquals("user-payload", cache.get("code-123"));

        cache.remove("code-123");
        assertNull(cache.get("code-123"));

        cache.add("code-expiring", "value");
        Thread.sleep(150);
        assertNull(cache.get("code-expiring"));
    }

    @Test
    void testTokenBlacklist() throws InterruptedException {
        TokenBlacklistImpl blacklist = new TokenBlacklistImpl();

        assertFalse(blacklist.isBlacklisted("jwt-id-1"));

        blacklist.add("jwt-id-1", 100, TimeUnit.MILLISECONDS);
        assertTrue(blacklist.isBlacklisted("jwt-id-1"));

        Thread.sleep(150);
        assertFalse(blacklist.isBlacklisted("jwt-id-1"));
    }

    @Test
    void testResetTokenCache() throws InterruptedException {
        ResetTokenCacheImpl cache = new ResetTokenCacheImpl();

        cache.add("token-xyz", "user@example.com", 100);
        assertEquals("user@example.com", cache.get("token-xyz"));

        cache.remove("token-xyz");
        assertNull(cache.get("token-xyz"));

        cache.add("token-expiring", "user@example.com", 100);
        Thread.sleep(150);
        assertNull(cache.get("token-expiring"));
    }

    @Test
    void testForgotPasswordCache() throws InterruptedException {
        ForgotPasswordCacheImpl cache = new ForgotPasswordCacheImpl();

        cache.add("user@example.com", "123456", 100);
        assertEquals("123456", cache.get("user@example.com"));

        cache.remove("user@example.com");
        assertNull(cache.get("user@example.com"));

        cache.add("user2@example.com", "654321", 100);
        Thread.sleep(150);
        assertNull(cache.get("user2@example.com"));
    }

    @Test
    void testCreateUrlRateLimit() {
        CreateUrlRateLimitImpl rateLimit = new CreateUrlRateLimitImpl(props);
        String key = "127.0.0.1:https://google.com";

        assertFalse(rateLimit.isLimited(key));

        rateLimit.add(key, Duration.ofSeconds(60));
        assertFalse(rateLimit.isLimited(key)); // 1 < 2

        rateLimit.add(key, Duration.ofSeconds(60));
        assertTrue(rateLimit.isLimited(key)); // 2 >= 2
    }

    @Test
    void testMailRateLimit() {
        MailRateLimitImpl mailLimit = new MailRateLimitImpl(props);
        String email = "test@example.com";

        assertFalse(mailLimit.isLimited(email));

        mailLimit.add(email, 60_000);
        mailLimit.add(email, 60_000);
        assertFalse(mailLimit.isLimited(email)); // 2 < 3

        mailLimit.add(email, 60_000);
        assertTrue(mailLimit.isLimited(email)); // 3 >= 3

        mailLimit.decrease(email);
        assertFalse(mailLimit.isLimited(email)); // 2 < 3
    }
}
