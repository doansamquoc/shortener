package dev.sam.shortener.service;

import com.nimbusds.jose.JOSEException;
import dev.sam.shortener.dto.request.JwtCreationRequest;

public interface JwtService {
	String generateToken(JwtCreationRequest request) throws JOSEException;
}
