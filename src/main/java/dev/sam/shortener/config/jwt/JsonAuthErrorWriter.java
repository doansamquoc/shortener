package dev.sam.shortener.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sam.shortener.enums.ErrorCode;
import dev.sam.shortener.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAuthErrorWriter implements AuthErrorWriter {
	ObjectMapper mapper;

	@Override
	public void writeError(HttpServletResponse response, ErrorCode ec, String path) throws IOException {
		String messaged = ec.getKey(); // Translate message here
		ErrorResponse error = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged);

		response.setStatus(ec.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		response.getWriter().write(mapper.writeValueAsString(error));
		response.flushBuffer();
	}
}
