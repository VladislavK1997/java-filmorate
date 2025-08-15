package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;


    @Override
    public Film add(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!exists(film.getId())) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Long id) {
        if (!exists(id)) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void remove(Long id) {
        if (!exists(id)) {
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        films.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        return films.containsKey(id);
    }
    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = getById(filmId);
        film.getLikes().add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film film = getById(filmId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
