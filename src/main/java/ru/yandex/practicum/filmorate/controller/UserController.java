package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserStorage userStorage;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!userStorage.existsById(user.getId())) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        User friend = userStorage.getById(friendId)
                .orElseThrow(() -> new NoSuchElementException("Друг не найден"));
        user.addFriend(friendId);
        friend.addFriend(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));
        User friend = userStorage.getById(friendId)
                .orElseThrow(() -> new NoSuchElementException("Друг не найден"));
        user.removeFriend(friendId);
        friend.removeFriend(id);
    }
}




