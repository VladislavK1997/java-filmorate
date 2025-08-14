package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class LikeController {

    private final FilmService filmService;
    private final UserService userService;

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        if (!filmService.exists(id)) {
            throw new NotFoundException("Фильм не найден: id=" + id);
        }
        if (!userService.exists(userId)) {
            throw new NotFoundException("Пользователь не найден: id=" + userId);
        }
        filmService.addLike(id, userId);
        log.info("Лайк добавлен: пользователь {} -> фильм {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        if (!filmService.exists(id)) {
            throw new NotFoundException("Фильм не найден: id=" + id);
        }
        if (!userService.exists(userId)) {
            throw new NotFoundException("Пользователь не найден: id=" + userId);
        }
        filmService.removeLike(id, userId);
        log.info("Лайк удалён: пользователь {} -> фильм {}", userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }
}