package dev.sam.shortener.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FieldViolation {
	String field;
	String message;
	String rejectedValue;

	public FieldViolation(String field, String message) {
		this.field = field;
		this.message = message;
		this.rejectedValue = null;
	}
}
