package dev.sam.shortener.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
        HttpServletRequest req,
        HttpServletResponse res,
        AuthenticationException e
    ) throws IOException {
        String targetUrl = Objects.requireNonNull(WebUtils.getCookie(req, "redirect_uri")).getValue();
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                                        .queryParam("error", e.getLocalizedMessage())
                                        .build()
                                        .toUriString();
        getRedirectStrategy().sendRedirect(req, res, targetUrl);
    }
}
