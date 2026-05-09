package dev.sam.shortener.dto.request;

import dev.sam.shortener.enums.MailTemplate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendTemplateMailRequest {
	String to;
	String subject;
	String body;
	MailTemplate mailTemplate;
	Map<String, Object> variables;
}
