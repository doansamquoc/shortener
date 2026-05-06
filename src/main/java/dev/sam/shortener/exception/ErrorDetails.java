package dev.sam.shortener.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetails {
	int code;
	String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	List<FieldViolation> fieldViolations;
}
