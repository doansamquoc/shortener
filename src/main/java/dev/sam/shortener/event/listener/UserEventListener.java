package dev.sam.shortener.event.listener;

import dev.sam.shortener.event.UserRegisteredEvent;
import dev.sam.shortener.service.SendMailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventListener {
	SendMailService sendMailService;

	@EventListener
	private void handleUserRegisteredEvent(UserRegisteredEvent event) {
		String to = event.user().getEmail();
		String subject = String.format("Welcome, %s!", event.user().getUsername());
		Map<String, Object> variables = new HashMap<>();
		variables.put("username", event.user().getUsername());
		variables.put("link", "https://shortener.com");

		sendMailService.sendWelcomeMail(to, subject, variables);
	}
}
