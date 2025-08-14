package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public User create(User user) {
        user.setId(nextId++);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        checkExists(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public void addFriend(int id, int friendId) {
        checkExists(id);
        checkExists(friendId);
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id); // взаимная дружба
    }

    public void removeFriend(int id, int friendId) {
        checkExists(id);
        checkExists(friendId);
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
    }

    public List<User> getFriends(int id) {
        checkExists(id);
        return users.get(id).getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        checkExists(id);
        checkExists(otherId);
        Set<Integer> friends1 = users.get(id).getFriends();
        Set<Integer> friends2 = users.get(otherId).getFriends();
        return friends1.stream()
                .filter(friends2::contains)
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    private void checkExists(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
    }

    private User getUser(int id) {
        return users.get(id);
    }
}