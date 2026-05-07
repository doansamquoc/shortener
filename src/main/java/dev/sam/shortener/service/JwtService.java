package dev.sam.shortener.service;

import com.nimbusds.jose.JOSEException;
import dev.sam.shortener.dto.CustomUserDetails;

public interface JwtService {
	String createToken(CustomUserDetails user) throws JOSEException;
}
