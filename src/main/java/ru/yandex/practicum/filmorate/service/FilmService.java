package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    public Film addFilm(Film film) {
        validateFilm(film);
        MpaRating mpa = getMpaRatingOrThrow(film.getMpa().getId());
        film.setMpa(mpa);
        validateGenres(film.getGenres());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        getFilm(film.getId());
        validateFilm(film);
        MpaRating mpa = getMpaRatingOrThrow(film.getMpa().getId());
        film.setMpa(mpa);
        validateGenres(film.getGenres());
        return filmStorage.updateFilm(film);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза не может быть пустой");
        }
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    private MpaRating getMpaRatingOrThrow(Long mpaId) {
        return mpaStorage.getMpaRatingById(mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id=" + mpaId + " не найден"));
    }

    private void validateGenres(Set<Genre> genres) {
        if (genres != null) {
            for (Genre genre : genres) {
                getGenreOrThrow(genre.getId());
            }
        }
    }

    private Genre getGenreOrThrow(Long genreId) {
        return genreStorage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с id=" + genreId + " не найден"));
    }

    public void deleteFilm(Long id) {
        if (!filmStorage.exists(id)) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        filmStorage.deleteFilm(id);
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        getFilm(filmId);
        getUserOrThrow(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        getFilm(filmId);
        getUserOrThrow(userId);
        boolean removed = filmStorage.removeLike(filmId, userId);
        if (!removed) {
            throw new NotFoundException("Лайк от пользователя с id=" + userId + " не найден");
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Количество фильмов должно быть положительным");
        }
        return filmStorage.getPopularFilms(count);
    }

    private void getUserOrThrow(Long id) {
        if (!userStorage.exists(id)) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }
}