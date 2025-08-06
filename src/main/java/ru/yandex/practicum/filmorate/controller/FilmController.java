package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!exists(film)) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлён фильм: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    private boolean exists(Film film) {
        return films.containsKey(film.getId());
    }
}