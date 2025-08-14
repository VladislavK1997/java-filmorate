package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    public User create(User user) {
        user.setId(nextId++);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (!exists(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        User existing = users.get(user.getId());
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setLogin(user.getLogin());
        existing.setBirthday(user.getBirthday());
        return existing;
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User getById(int id) {
        if (!exists(id)) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    public boolean exists(int id) {
        return users.containsKey(id);
    }

    public void addFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public boolean removeFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        if (!user.getFriends().contains(friendId)) {
            return false;
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return true;
    }

    public List<User> getFriends(int userId) {
        User user = getById(userId);
        List<User> friends = new ArrayList<>();
        for (Integer friendId : user.getFriends()) {
            friends.add(getById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = getById(userId);
        User other = getById(otherId);

        Set<Integer> commonIds = new HashSet<>(user.getFriends());
        commonIds.retainAll(other.getFriends());

        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonIds) {
            commonFriends.add(getById(id));
        }
        return commonFriends;
    }
}