package dev.sam.shortener.event;

import dev.sam.shortener.dto.request.ClickRequest;

public record UrlClickedEvent(String shortCode, ClickRequest request) {}
