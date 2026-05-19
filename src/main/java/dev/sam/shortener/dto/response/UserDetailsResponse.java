package dev.sam.shortener.dto.response;

import dev.sam.shortener.entity.UserRole;

import java.time.Instant;
import java.util.Set;

public record UserDetailsResponse(
Long id,
String username,
String email,
Set<UserRole> roles,
Instant createdAt,
Instant updatedAt
) {}
