package dev.sam.shortener.exception;

import dev.sam.shortener.config.TranslateMessageService;
import dev.sam.shortener.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
	TranslateMessageService translator;

	@ExceptionHandler(AppException.class)
	ResponseEntity<ErrorResponse> handleAppException(AppException e, Locale locale) {
		ErrorCode ec = e.getErrorCode();

		// Translate for each message field
		List<FieldViolation> fieldViolations = e.getFieldViolations()
			.stream()
			.filter(Objects::nonNull)
			.map(f -> {
				String message = translator.of(f.getMessage(), null, locale);
				return new FieldViolation(f.getField(), message);
			})
			.toList();

		// Translate the main message
		String message = translator.of(ec.getKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(
			ec.getStatus(),
			ec.getCode(),
			message,
			fieldViolations
		);

		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
		Locale locale
	) {
		List<FieldViolation> violations = e.getBindingResult().getFieldErrors().stream().map(f -> {
			String message = translator.of(f.getDefaultMessage(), null, locale);
			return new FieldViolation(f.getField(), message);
		}).toList();

		ErrorCode ec = ErrorCode.VALIDATION_FAILED;
		String message = translator.of(ec.getKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(
			ec.getStatus(),
			ec.getCode(),
			message,
			violations
		);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> handleException(Exception e, Locale locale) {
		log.error(e.getMessage(), e);
		ErrorCode ec = ErrorCode.SERVER_INTERNAL;
		String message = translator.of(ec.getKey(), null, locale);
		ErrorResponse response = ErrorResponse.of(ec.getStatus(), ec.getCode(), message);
		return ResponseEntity.status(ec.getStatus()).body(response);
	}
}
