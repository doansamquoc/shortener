package dev.sam.shortener.service;

import dev.sam.shortener.dto.request.SendMailRequest;
import dev.sam.shortener.enums.MailTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface MailService {
	void sendSimpleMail(SendMailRequest request);

	@Async("mailExecutor")
	void sendTemplateMail(String to, String subject, MailTemplate template, Map<String, Object> variables);
}
