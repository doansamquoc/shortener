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
