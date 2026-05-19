package dev.sam.shortener.event;

import dev.sam.shortener.entity.User;

public record UserRegisteredEvent(User user) {}
