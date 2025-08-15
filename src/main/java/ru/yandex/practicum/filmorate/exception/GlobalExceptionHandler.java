package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(Exception e) {
        String msg = e.getMessage();
        if (e instanceof MethodArgumentNotValidException manve) {
            if (manve.getBindingResult() != null && manve.getBindingResult().getFieldError() != null) {
                msg = manve.getBindingResult().getFieldError().getDefaultMessage();
            }
        }
        return msg == null ? "Ошибка валидации" : msg;
    }

    @ExceptionHandler({NotFoundException.class, UserNotFoundException.class, FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RuntimeException e) {
        return e.getMessage() == null ? "Объект не найден" : e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOther(Exception e) {
        return e.getMessage() == null ? "Внутренняя ошибка сервера" : e.getMessage();
    }
}
