package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getAllFilms();
        films.sort((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()));
        return films.subList(0, Math.min(count, films.size()));
    }
}
