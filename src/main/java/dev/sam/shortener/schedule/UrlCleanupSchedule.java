package dev.sam.shortener.schedule;

import dev.sam.shortener.service.UrlService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UrlCleanupSchedule {
	UrlService  urlService;

	// At 0:00 every day
	@Scheduled(cron = "0 0 0 * * *")
	public void cleanOldUrls() {
		log.info("Start clean old urls");
		urlService.cleanupUrls();
	}
}
