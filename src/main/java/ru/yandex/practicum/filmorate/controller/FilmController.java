package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }

        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!exists(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }

        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }

        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    private boolean exists(int id) {
        return films.containsKey(id);
    }
}