package dev.sam.shortener.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
	SERVER_INTERNAL(5001, "server.internal", HttpStatus.INTERNAL_SERVER_ERROR.value()),

	// AUTHENTICATION
	AUTH_UNAUTHORIZED(4001, "auth.unauthorized", HttpStatus.UNAUTHORIZED.value()),
	AUTH_ACCESS_DENIED(4003, "auth.denied", HttpStatus.FORBIDDEN.value()),

	// USER
	USER_EMAIL_EXISTS(2001, "user.email.exists", HttpStatus.CONFLICT.value()),
	USER_USERNAME_EXISTS(2002, "user.username.exists", HttpStatus.CONFLICT.value()),

	URL_CODE_EXISTS(1001, "url.short_code.exists", HttpStatus.CONFLICT.value()),
	URL_NOT_FOUND(1002, "url.not_found", HttpStatus.NOT_FOUND.value()),
	;

	// Error code
	int code;

	// Message key, will be translated by MessageSource
	String key;

	// HTTP status
	int status;
}
