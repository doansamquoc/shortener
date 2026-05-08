package dev.sam.shortener.dto.request;

import java.util.Set;

public record JwtCreationRequest(Long id, String username, Set<String> roles) {}
