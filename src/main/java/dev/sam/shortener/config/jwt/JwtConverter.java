package dev.sam.shortener.config.jwt;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static dev.sam.shortener.constant.AppConstant.AUTHORIZE_CLAIM_NAME;
import static dev.sam.shortener.constant.AppConstant.AUTHORIZE_PREFIX;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	JwtGrantedAuthoritiesConverter authoritiesConverter;

	public JwtConverter() {
		this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
		this.authoritiesConverter.setAuthoritiesClaimName(AUTHORIZE_CLAIM_NAME);
		this.authoritiesConverter.setAuthorityPrefix(AUTHORIZE_PREFIX);
	}

	@Override
	public @Nullable AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
		Collection<GrantedAuthority> authorities = this.authoritiesConverter.convert(jwt);
		return new JwtAuthenticationToken(jwt, authorities);
	}
}
