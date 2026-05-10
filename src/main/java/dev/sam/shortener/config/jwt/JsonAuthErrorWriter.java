package dev.sam.shortener.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sam.shortener.config.TranslateMessageService;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAuthErrorWriter implements AuthErrorWriter {
	ObjectMapper mapper;
	TranslateMessageService translate;

	@Override
	public void writeError(HttpServletResponse response, ErrorCode ec, String path) throws IOException {
		Locale locale = LocaleContextHolder.getLocale();
		String messaged = translate.of(ec.getKey(), null, locale);
		ErrorResponse error = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged);

		response.setStatus(ec.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		response.getWriter().write(mapper.writeValueAsString(error));
		response.flushBuffer();
	}
}
