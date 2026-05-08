package dev.sam.shortener.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.dto.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

import static dev.sam.shortener.constant.AppConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {
	AppProperties props;
	SecretKey secretKey;

	@Override
	public String createToken(CustomUserDetails user) throws JOSEException {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		Payload payload =new Payload(prepareClaims(user).toJSONObject());
		JWSObject jwsObject = new JWSObject(header,payload);
		jwsObject.sign(new MACSigner(secretKey));
		return jwsObject.serialize();
	}

	private Set<String> getRoles(Collection<? extends GrantedAuthority> authorities) {
		log.info("getRoles authorities: {}", authorities);
		return authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.filter(Objects::nonNull)
			.map(a -> a.replace(AUTHORIZE_PREFIX, ""))
			.collect(Collectors.toSet());
	}

	private JWTClaimsSet prepareClaims(CustomUserDetails user) {
		Date issueTime = new Date();
		Date expirationTime = new Date(System.currentTimeMillis() + props.getAccessTokenExpiration());
		Set<String> roles = getRoles(user.getAuthorities());

		return new JWTClaimsSet.Builder()
			.issuer("URL Shortener API")
			.subject(String.valueOf(user.getId()))
			.issueTime(issueTime)
			.expirationTime(expirationTime)
			.jwtID(UUID.randomUUID().toString())
			.claim(AUTHORIZE_CLAIM_NAME, roles)
			.claim(JWT_USERNAME_CLAIM_NAME, user.getUsername())
			.build();
	}
}
