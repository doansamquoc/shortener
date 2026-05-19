package dev.sam.shortener.dto.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public record ApiResponse<T>(T data, String message, LocalDateTime timestamp) {
	public static <T> ApiResponse<T> of(T data, String message) {
		return new ApiResponse<>(data, message, LocalDateTime.now());
	}

	// Data and default message
	public static <T> ApiResponse<T> of(T data) {
		return new ApiResponse<>(data, "Successfully", LocalDateTime.now());
	}

	// Message only
	public static ApiResponse<String> of(String message) {
		return new ApiResponse<>(message, null, LocalDateTime.now());
	}

	// No content
	public static ApiResponse<?> of() {
		return new ApiResponse<>(null, "Successfully", null);
	}
}
