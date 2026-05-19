package dev.sam.shortener.dto;

import dev.sam.shortener.entity.RefreshToken;

public record TokenDto(
    String accessToken,
    RefreshToken refreshToken
) {}
