package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        Film createdFilm = filmStorage.addFilm(film);
        log.info("Добавлен новый фильм с ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.exists(film.getId())) {
            log.warn("Фильм с ID {} не найден", film.getId());
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        validateFilm(film);
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Обновлен фильм с ID: {}", film.getId());
        return updatedFilm;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза не может быть пустой");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    public void deleteFilm(Long id) {
        if (!filmStorage.exists(id)) {
            log.warn("Попытка удаления несуществующего фильма с ID: {}", id);
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        filmStorage.deleteFilm(id);
        log.info("Удален фильм с ID: {}", id);
    }

    public Film getFilm(Long id) {
        Film film = filmStorage.getFilm(id)
                .orElseThrow(() -> {
                    log.warn("Запрошен несуществующий фильм с ID: {}", id);
                    return new NotFoundException("Фильм с id=" + id + " не найден");
                });
        log.info("Запрошен фильм с ID: {}", id);
        return film;
    }

    public List<Film> getAllFilms() {
        log.info("Запрошен список всех фильмов");
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.exists(filmId)) {
            log.warn("Попытка добавить лайк несуществующему фильму с ID: {}", filmId);
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        if (!userService.exists(userId)) {
            log.warn("Попытка добавить лайк от несуществующего пользователя с ID: {}", userId);
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        filmStorage.addLike(filmId, userId);
        log.info("Добавлен лайк фильму {} от пользователя {}", filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (!filmStorage.exists(filmId)) {
            log.warn("Попытка удалить лайк у несуществующего фильма с ID: {}", filmId);
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        if (!userService.exists(userId)) {
            log.warn("Попытка удалить лайк несуществующего пользователя с ID: {}", userId);
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        if (!filmStorage.removeLike(filmId, userId)) {
            log.warn("Попытка удалить несуществующий лайк от пользователя {} фильму {}", userId, filmId);
            throw new NotFoundException("Лайк от пользователя с id=" + userId + " не найден");
        }
        log.info("Удален лайк фильму {} от пользователя {}", filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            log.warn("Запрошено некорректное количество популярных фильмов: {}", count);
            throw new ValidationException("Количество фильмов должно быть положительным");
        }
        log.info("Запрошены {} самых популярных фильмов", count);
        return filmStorage.getPopularFilms(count);
    }
}