package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        validate(user);
        normalize(user);
        return userStorage.add(user);
    }

    public User update(User user) {
        validate(user);
        normalize(user);
        if (!userStorage.exists(user.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.update(user);
    }

    public User get(Long id) {
        if (!userStorage.exists(id)) throw new NotFoundException("Пользователь не найден");
        return userStorage.get(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public List<User> getFriends(Long userId) {
        User user = get(userId);
        return user.getFriends().stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user1 = get(userId);
        User user2 = get(otherId);
        Set<Long> common = new HashSet<>(user1.getFriends());
        common.retainAll(user2.getFriends());
        return common.stream().map(this::get).collect(Collectors.toList());
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email должен быть валидным");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы");
        }
        if (user.getBirthday() == null) {
            throw new ValidationException("Дата рождения не может быть пустой");
        }
        if (user.getBirthday().isAfter(java.time.LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
