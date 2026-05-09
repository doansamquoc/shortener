package dev.sam.shortener.security.oauth2;

import dev.sam.shortener.cache.AuthTemporaryCode;
import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	AuthTemporaryCode authTemporaryCode;
	AppProperties props;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		String code = UUID.randomUUID().toString();
		authTemporaryCode.add(code, user);

		String targetUrl = UriComponentsBuilder.fromUriString(props.getFrontendRedirectUrl())
		.queryParam("code", code).build().toUriString();

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
