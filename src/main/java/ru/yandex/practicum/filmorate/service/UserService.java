package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User add(User user) {
        return storage.add(user);
    }

    public User update(User user) {
        if (!storage.exists(user.getId())) throw new NotFoundException("User not found");
        return storage.update(user);
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