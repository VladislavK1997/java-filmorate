package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        if (!userStorage.exists(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        return userStorage.update(user);
    }

    public User getById(Long id) {
        if (!userStorage.exists(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void remove(Long id) {
        if (!userStorage.exists(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        userStorage.remove(id);
    }

    public boolean exists(Long id) {
        return userStorage.exists(id);
    }
}

