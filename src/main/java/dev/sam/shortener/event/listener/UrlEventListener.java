package dev.sam.shortener.event.listener;

import dev.sam.shortener.dto.response.UrlResponse;
import dev.sam.shortener.event.UrlClickedEvent;
import dev.sam.shortener.service.ClickService;
import dev.sam.shortener.service.UrlService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UrlEventListener {
    UrlService urlService;
    ClickService clickService;
    
    @EventListener
    @Async("clickExecutor")
    public void handleUrlClicked(UrlClickedEvent event) {
        UrlResponse response = urlService.getUrl(event.shortCode());
        urlService.incrementTotalClicks(event.shortCode());
        clickService.create(response.id(), event.request());
    }
}