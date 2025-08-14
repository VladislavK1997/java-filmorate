package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!filmStorage.existsById(film.getId())) {
            throw new NoSuchElementException("Фильм не найден");
        }
        return filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден"));
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден"));
        film.addLike(userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Фильм не найден"));
        film.removeLike(userId);
    }
}


