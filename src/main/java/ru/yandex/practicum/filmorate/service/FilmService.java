package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class FilmService {

    private static final LocalDate CINEMA_BIRTH = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        validate(film);
        if (!filmStorage.exists(film.getId())) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        if (!filmStorage.exists(id)) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        return filmStorage.get(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getById(filmId);
        if (!userStorage.exists(userId)) {
            throw new ValidationException("Пользователь для лайка не найден");
        }
        film.addLike(userId);
        filmStorage.update(film);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getById(filmId);
        film.removeLike(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(int count) {
        if (count <= 0) count = 10;
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed()
                        .thenComparing(f -> Objects.requireNonNullElse(f.getId(), 0L)))
                .limit(count)
                .toList();
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза не может быть пустой");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTH)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
