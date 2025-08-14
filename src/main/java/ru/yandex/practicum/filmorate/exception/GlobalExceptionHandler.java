package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> messages = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        err -> err.getField(),
                        err -> err.getDefaultMessage(),
                        (a, b) -> a + "; " + b
                ));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", "400");
        body.put("error", "Validation failed");
        body.put("messages", messages);

        log.warn("Validation failed: {}", messages);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleCustomValidation(RuntimeException ex) {
        Map<String, String> error = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", "400",
                "error", "Bad Request",
                "message", ex.getMessage()
        );
        log.warn("Bad request: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException ex) {
        Map<String, String> error = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", "404",
                "error", "Not Found",
                "message", ex.getMessage()
        );
        log.warn("Not found: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        log.error("Internal server error", ex);
        Map<String, String> error = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", "500",
                "error", "Internal Server Error",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
