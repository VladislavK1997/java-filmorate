package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public boolean exists(Long userId) {
        return userStorage.exists(userId);
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User createdUser = userStorage.addUser(user);
        log.info("Добавлен новый пользователь с ID: {}", createdUser.getId());
        return createdUser;
    }

    public User updateUser(User user) {
        if (!userStorage.exists(user.getId())) {
            log.warn("Пользователь с ID {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        User updatedUser = userStorage.updateUser(user);
        log.info("Обновлен пользователь с ID: {}", user.getId());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        if (!userStorage.exists(id)) {
            log.warn("Попытка удаления несуществующего пользователя с ID: {}", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        userStorage.deleteUser(id);
        log.info("Удален пользователь с ID: {}", id);
    }

    public User getUser(Long id) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> {
                    log.warn("Запрошен несуществующий пользователь с ID: {}", id);
                    return new NotFoundException("Пользователь с id=" + id + " не найден");
                });
        log.info("Запрошен пользователь с ID: {}", id);
        return user;
    }

    public List<User> getAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        if (!userStorage.exists(userId) || !userStorage.exists(friendId)) {
            log.warn("Попытка добавить друзей с несуществующими ID: {} или {}", userId, friendId);
            throw new NotFoundException("Один из пользователей не найден");
        }
        userStorage.addFriend(userId, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (!userStorage.exists(userId) || !userStorage.exists(friendId)) {
            log.warn("Попытка удалить друзей с несуществующими ID: {} или {}", userId, friendId);
            throw new NotFoundException("Один из пользователей не найден");
        }
        userStorage.removeFriend(userId, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
    }

    public List<User> getFriends(Long id) {
        if (!userStorage.exists(id)) {
            log.warn("Запрошены друзья несуществующего пользователя с ID: {}", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        log.info("Запрошены друзья пользователя с ID: {}", id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        if (!userStorage.exists(id) || !userStorage.exists(otherId)) {
            log.warn("Запрошены общие друзья несуществующих пользователей с ID: {} или {}", id, otherId);
            throw new NotFoundException("Один из пользователей не найден");
        }
        log.info("Запрошены общие друзья пользователей {} и {}", id, otherId);
        return userStorage.getCommonFriends(id, otherId);
    }
}