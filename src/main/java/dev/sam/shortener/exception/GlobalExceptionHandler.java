package dev.sam.shortener.exception;

import dev.sam.shortener.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
	@ExceptionHandler(AppException.class)
	ResponseEntity<ErrorResponse> handleAppException(AppException e) {
		ErrorCode ec = e.getErrorCode();

		// Translate for each message field
		List<FieldViolation> fieldViolations = e.getFieldViolations().stream().filter(Objects::nonNull).map(f -> {
			String message = f.getMessage(); // Translate message here
			return new FieldViolation(f.getField(), message);
		}).toList();

		// Translate the main message
		String message = ec.getKey(); // Translate here
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), message, fieldViolations);

		return ResponseEntity.status(ec.getStatus()).body(response);
	}
}
