package dev.sam.shortener.controller;

import dev.sam.shortener.service.UrlService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppController {
	UrlService service;

	@GetMapping("/redirect/{shortCode}")
	public ResponseEntity<Void> redirect(
		@PathVariable String shortCode,
		@RequestHeader(value = "User-Agent", defaultValue = "Unknown") String userAgent,
		@RequestHeader(value = "Referer", required = false) String referer
	) {
		// Clicks record here
		log.info("Referer {}", referer);
		log.info("User agent {}", userAgent);

		String url = service.getRedirectUrl(shortCode);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}
}
