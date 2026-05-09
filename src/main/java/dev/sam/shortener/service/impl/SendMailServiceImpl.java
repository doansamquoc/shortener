package dev.sam.shortener.service.impl;

import dev.sam.shortener.cache.MailRateLimit;
import dev.sam.shortener.dto.request.SendMailRequest;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.enums.MailTemplate;
import dev.sam.shortener.exception.AppException;
import dev.sam.shortener.service.MailService;
import dev.sam.shortener.service.SendMailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMailServiceImpl implements SendMailService {
	MailService mailService;
	MailRateLimit mailRateLimit;

	@Override
	public void sendSimpleMail(SendMailRequest request) {
		if (mailRateLimit.isLimited(request.getTo())) throw AppException.of(ErrorCode.MAIL_LIMITED);
		mailService.sendSimpleMail(request);
	}

	@Override
	public void sendWelcomeMail(String to, String subject, Map<String, Object> variables) {
		mailService.sendTemplateMail(to, subject, MailTemplate.WELCOME, variables);
	}

	@Override
	public void sendForotPasswordMail(String to, String subject, Map<String, Object> variables) {
		mailService.sendTemplateMail(to, subject, MailTemplate.FORGOT_PASSWORD, variables);
	}
}
