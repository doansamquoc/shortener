package dev.sam.shortener.service;

import dev.sam.shortener.dto.request.SendMailRequest;

import java.util.Map;

public interface SendMailService {
	void sendSimpleMail(SendMailRequest request);

	void sendWelcomeMail(String to, String subject, Map<String, Object> variables);
}
