package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User get(Long id) {
        User user = userStorage.get(id);
        if (user == null) throw new NotFoundException("User not found");
        return user;
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
}