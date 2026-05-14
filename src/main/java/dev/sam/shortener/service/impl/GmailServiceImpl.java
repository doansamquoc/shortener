package dev.sam.shortener.service.impl;

import dev.sam.shortener.cache.MailRateLimit;
import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.dto.request.SendMailRequest;
import dev.sam.shortener.enums.MailTemplate;
import dev.sam.shortener.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GmailServiceImpl implements MailService {
    AppProperties props;
    JavaMailSender sender;
    TemplateEngine engine;
    MailRateLimit mailRateLimit;
    
    @Override
    @Async("mailExecutor")
    public void sendSimpleMail(SendMailRequest request) {
        mailRateLimit.add(request.getTo(), props.getEmail().getLimitDuration());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(props.getEmail().getFrom());
            message.setTo(request.getTo());
            message.setSubject(request.getSubject());
            message.setText(request.getBody());
            
            if (request.getCc() != null) message.setCc(request.getCc());
            if (request.getBcc() != null) message.setBcc(request.getBcc());
            
            sender.send(message);
            log.info("Mail sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            mailRateLimit.decrease(request.getTo());
            log.error("Mail sent failed to: {}", request.getTo(), e);
        }
    }
    
    @Override
    @Async("mailExecutor")
    public void sendTemplateMail(String to, String subject, MailTemplate template, Map<String, Object> variables) {
        try {
            // Thymeleaf initializing
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = engine.process(template.getPath(), context);
            
            // MimeMessage initializing
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(props.getEmail().getFrom());
            helper.setReplyTo(props.getEmail().getReplyTo());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            // Send mail
            sender.send(message);
            log.info("{} mail sent successfully to: {}", template.name(), to);
        } catch (MessagingException e) {
            log.error("{} mail sent failed to: {}", template.name(), to, e);
        }
    }
}
