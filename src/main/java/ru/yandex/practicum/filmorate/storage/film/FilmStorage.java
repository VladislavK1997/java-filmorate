package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);
    Film update(Film film);

    Optional<Film> getById(int id);
    
    Collection<Film> getAll();

    default boolean existsById(int id) {
        return getById(id).isPresent();
    }
}
