package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1L;

    @Override
    public Film add(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException("Film with id " + film.getId() + " not found");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(Long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NoSuchElementException("Film with id " + id + " not found");
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean exists(Long id) {
        return films.containsKey(id);
    }
}


