package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        getUserOrThrow(user.getId());
        return userStorage.updateUser(user);
    }

    public void deleteUser(Long id) {
        getUserOrThrow(id);
        userStorage.deleteUser(id);
    }

    public User getUser(Long id) {
        return getUserOrThrow(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        getUserOrThrow(userId);
        getUserOrThrow(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserOrThrow(userId);
        getUserOrThrow(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long id) {
        getUserOrThrow(id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        getUserOrThrow(id);
        getUserOrThrow(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    private User getUserOrThrow(Long id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }
}