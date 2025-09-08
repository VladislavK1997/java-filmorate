package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void shouldNotAllowEmptyName() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Film film = new Film();
            film.setName("");
            film.setDescription("Тест");
            film.setReleaseDate(LocalDate.of(2000, 1, 1));
            film.setDuration(90);

            Set<ConstraintViolation<Film>> violations = validator.validate(film);
            assertFalse(violations.isEmpty(), "Фильм с пустым названием должен вызывать ошибки валидации");
        }
    }
}
