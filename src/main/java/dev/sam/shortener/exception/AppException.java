package dev.sam.shortener.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.sam.shortener.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException {
    ErrorCode errorCode;
    List<FieldViolation> fieldViolations;
    String[] args;
    
    private AppException(ErrorCode errorCode, List<FieldViolation> fieldViolations, String[] args) {
        this.errorCode = errorCode;
        this.fieldViolations = fieldViolations;
        this.args = args;
    }
    
    public static AppException of(ErrorCode errorCode, List<FieldViolation> fieldViolations, String[] args) {
        return new AppException(errorCode, fieldViolations, args);
    }
    
    public static AppException of(ErrorCode errorCode, List<FieldViolation> fieldViolations) {
        return new AppException(errorCode, fieldViolations, null);
    }
    
    public static AppException of(ErrorCode errorCode) {
        return new AppException(errorCode, Collections.emptyList(), null);
    }
}
