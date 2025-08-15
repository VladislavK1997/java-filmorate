package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 1;

    public User add(User user) {
        return storage.add(user);
    }
    public User createUser(User user) {
        validateUser(user);
        user.setId(idCounter++);
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("User not found");
        }
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login cannot be empty or contain spaces");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Email is invalid");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday cannot be in the future");
        }
    }

    public User getById(Long id) {
        User user = storage.getById(id);
        if (user == null) throw new NotFoundException("User not found");
        return user;
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = getById(userId);
        return user.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getById(userId);
        User other = getById(otherId);
        Set<Long> common = new HashSet<>(user.getFriends());
        common.retainAll(other.getFriends());
        return common.stream().map(this::getById).collect(Collectors.toList());
    }
}