package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private Long id;


    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание фильма не должно превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    private Set<Long> likes = new HashSet<>();

    public void setReleaseDate(LocalDate releaseDate) {
        LocalDate firstFilmEver = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(firstFilmEver)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        this.releaseDate = releaseDate;
    }
}