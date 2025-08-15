package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Film not found");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    public Film getById(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) throw new NotFoundException("Film not found");
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Film name cannot be empty");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Description is too long");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date is invalid");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Duration must be positive");
        }
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(Long filmId, Long userId) {
        if (!userStorage.exists(userId)) throw new NotFoundException("User not found");
        Film film = getById(filmId);
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (!userStorage.exists(userId)) throw new NotFoundException("User not found");
        Film film = getById(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}

