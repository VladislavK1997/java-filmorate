package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Создан пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!exists(user)) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Обновлён пользователь: {}", user);
        return user;
    }

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    private boolean exists(User user) {
        return users.containsKey(user.getId());
    }
}
