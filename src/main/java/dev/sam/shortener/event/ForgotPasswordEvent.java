package dev.sam.shortener.event;

import dev.sam.shortener.entity.User;

public record ForgotPasswordEvent(User user, String code) {}
