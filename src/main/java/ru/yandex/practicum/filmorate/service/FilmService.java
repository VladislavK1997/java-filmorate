package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>(); // filmId -> userId
    private int nextId = 1;

    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    public Film create(Film film) {
        validate(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    public Film update(Film film) {
        validate(film);
        checkExists(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> getPopular(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(likes.get(f2.getId()).size(), likes.get(f1.getId()).size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания не более 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    private void checkExists(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
    }

    public void addLike(int filmId, int userId) {
        checkExists(filmId);
        likes.get(filmId).add(userId);
    }

    public void removeLike(int filmId, int userId) {
        checkExists(filmId);
        likes.get(filmId).remove(userId);
    }
}