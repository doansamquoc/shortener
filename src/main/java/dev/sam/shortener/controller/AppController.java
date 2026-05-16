package dev.sam.shortener.controller;

import dev.sam.shortener.dto.request.ClickRequest;
import dev.sam.shortener.event.UrlClickedEvent;
import dev.sam.shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppController {
	UrlService service;
	ApplicationEventPublisher publisher;
	
	@GetMapping("/{shortCode}")
	public ResponseEntity<Void> redirect(
		@PathVariable String shortCode,
		HttpServletRequest request
	) {
		String url = service.getRedirectUrl(shortCode);
		ClickRequest clickRequest = makeRequest(request);
		publisher.publishEvent(new UrlClickedEvent(shortCode, clickRequest));
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}
	
	private ClickRequest makeRequest(HttpServletRequest request) {
		String ipAddress = getIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		String referer = request.getHeader("Referer");
		return ClickRequest.builder().ipAddress(ipAddress).userAgent(userAgent).referrer(referer).build();
	}
	
	private String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) ipAddress = request.getRemoteAddr();
		return ipAddress;
	}
}
