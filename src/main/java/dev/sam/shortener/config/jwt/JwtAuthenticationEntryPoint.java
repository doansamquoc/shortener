package dev.sam.shortener.config.jwt;

import dev.sam.shortener.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	AuthErrorWriter writer;

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		writer.writeError(response, ErrorCode.AUTH_UNAUTHORIZED, request.getServletPath());
	}
}
