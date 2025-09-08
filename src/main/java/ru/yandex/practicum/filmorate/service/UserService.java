package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        getUserOrThrow(user.getId()); // Используем get для проверки
        return userStorage.updateUser(user);
    }

    public void deleteUser(Long id) {
        if (!userStorage.exists(id)) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        userStorage.deleteUser(id);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        getUserOrThrow(userId);    // Используем get для проверки
        getUserOrThrow(friendId);  // Используем get для проверки
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserOrThrow(userId);    // Используем get для проверки
        getUserOrThrow(friendId);  // Используем get для проверки
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        getUserOrThrow(id); // Используем get для проверки
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        getUserOrThrow(id);      // Используем get для проверки
        getUserOrThrow(otherId); // Используем get для проверки
        return userStorage.getCommonFriends(id, otherId);
    }

    private User getUserOrThrow(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}