package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        Film created = filmStorage.create(film);
        log.info("Добавлен фильм: {}", created);
        return created;
    }

    public Film update(Film film) {
        getById(film.getId());
        Film updated = filmStorage.update(film);
        log.info("Обновлён фильм: {}", updated);
        return updated;
    }

    public Film getById(int id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден: id=" + id));
    }

    public List<Film> getAll() {
        return filmStorage.getAll().stream().toList();
    }

    public void addLike(int filmId, int userId) {
        Film film = getById(filmId);
        userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: id=" + userId));

        film.getLikes().add(userId);
        filmStorage.update(film);
        log.info("Пользователь {} лайкнул фильм {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getById(filmId);
        film.getLikes().remove(userId);
        filmStorage.update(film);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed()
                        .thenComparing(Film::getName))
                .limit(count)
                .toList();
    }
    public boolean exists(int id) {
        return getById(id) != null;
    }
}