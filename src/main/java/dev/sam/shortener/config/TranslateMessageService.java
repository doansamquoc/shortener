package dev.sam.shortener.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslateMessageService {
	MessageSource messageSource;
	public String of(String key, Object[] args, Locale locale) {
		return messageSource.getMessage(key, args, locale);
	}
}
