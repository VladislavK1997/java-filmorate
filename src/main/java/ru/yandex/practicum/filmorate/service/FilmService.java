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
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        log.info("Добавление нового фильма: {}", film.getName());
        Film createdFilm = filmStorage.addFilm(film);
        log.info("Фильм успешно добавлен с ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        getFilmOrThrow(film.getId());
        validateFilm(film);
        log.info("Обновление фильма с ID: {}", film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Фильм с ID {} успешно обновлен", film.getId());
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
        getFilmOrThrow(id);
        log.info("Удаление фильма с ID: {}", id);
        filmStorage.deleteFilm(id);
        log.info("Фильм с ID {} успешно удален", id);
    }

    public Film getFilm(Long id) {
        log.info("Получение фильма с ID: {}", id);
        return getFilmOrThrow(id);
    }

    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getFilmOrThrow(filmId);
        log.info("Добавление лайка фильму ID {} от пользователя ID {}", filmId, userId);
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
        log.info("Лайк успешно добавлен");
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getFilmOrThrow(filmId);
        log.info("Удаление лайка фильму ID {} от пользователя ID {}", filmId, userId);
        if (!film.getLikes().remove(userId)) {
            log.warn("Лайк от пользователя ID {} не найден у фильма ID {}", userId, filmId);
            throw new NotFoundException("Лайк от пользователя с id=" + userId + " не найден");
        }
        filmStorage.updateFilm(film);
        log.info("Лайк успешно удален");
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получение {} самых популярных фильмов", count);
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть положительным");
        }
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film getFilmOrThrow(Long id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }
}