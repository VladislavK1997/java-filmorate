package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (!filmStorage.exists(film.getId())) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        return filmStorage.update(film);
    }

    public Film getById(Long id) {
        if (!filmStorage.exists(id)) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        return filmStorage.getById(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void remove(Long id) {
        if (!filmStorage.exists(id)) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        filmStorage.remove(id);
    }

    public boolean exists(Long id) {
        return filmStorage.exists(id);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
