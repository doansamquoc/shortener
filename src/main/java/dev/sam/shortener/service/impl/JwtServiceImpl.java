package dev.sam.shortener.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import dev.sam.shortener.config.AppProperties;
import dev.sam.shortener.dto.request.JwtCreationRequest;
import dev.sam.shortener.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

import static dev.sam.shortener.constant.AppConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {
	AppProperties props;
	SecretKey secretKey;

	@Override
	public String generateToken(JwtCreationRequest request) throws JOSEException {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		Payload payload = generatePayload(request);
		JWSObject jwsObject = new JWSObject(header, payload);
		jwsObject.sign(new MACSigner(secretKey));
		return jwsObject.serialize();
	}

	private Payload generatePayload(JwtCreationRequest request) {
		Date issueTime = new Date();
		Date expirationTime = new Date(System.currentTimeMillis() + props.getAccessTokenExpiration());

		JWTClaimsSet claims = new JWTClaimsSet.Builder()
		.issuer("URL Shortener API")
		.subject(String.valueOf(request.id()))
		.issueTime(issueTime)
		.expirationTime(expirationTime)
		.jwtID(UUID.randomUUID().toString())
		.claim(AUTHORIZE_CLAIM_NAME, request.roles())
		.claim(JWT_USERNAME_CLAIM_NAME, request.username())
		.build();

		return new Payload(claims.toJSONObject());
	}
}
