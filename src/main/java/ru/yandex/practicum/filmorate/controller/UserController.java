package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!userService.exists(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (!userService.exists(id) || !userService.exists(friendId)) {
            throw new NotFoundException("Пользователь с id " + id + " или друг с id " + friendId + " не найден");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        if (!userService.exists(id) || !userService.exists(friendId)) {
            throw new NotFoundException("Пользователь с id " + id + " или друг с id " + friendId + " не найден");
        }

        boolean removed = userService.removeFriend(id, friendId);
        if (!removed) {
            throw new NotFoundException("Связь между пользователем " + id + " и другом " + friendId + " не найдена");
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        if (!userService.exists(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (!userService.exists(id) || !userService.exists(otherId)) {
            throw new NotFoundException("Пользователь с id " + id + " или пользователь с id " + otherId + " не найден");
        }
        return userService.getCommonFriends(id, otherId);
    }
}


