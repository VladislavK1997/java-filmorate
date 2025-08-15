package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUser(userId);
        List<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriends = userStorage.getUser(userId).getFriends();
        Set<Long> otherFriends = userStorage.getUser(otherId).getFriends();
        Set<Long> commonIds = new HashSet<>(userFriends);
        commonIds.retainAll(otherFriends);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : commonIds) {
            commonFriends.add(userStorage.getUser(id));
        }
        return commonFriends;
    }
}


