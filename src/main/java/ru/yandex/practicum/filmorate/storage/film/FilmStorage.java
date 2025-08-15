package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    Film getById(Long id);

    List<Film> getAll();

    void remove(Long id);

    boolean exists(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getPopularFilms(int count);
}
