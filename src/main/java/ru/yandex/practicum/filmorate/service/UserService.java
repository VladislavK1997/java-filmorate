package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        User created = userStorage.create(user);
        log.info("Создан пользователь: {}", created);
        return created;
    }

    public User update(User user) {
        User existed = getById(user.getId());
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        User updated = userStorage.update(user);
        log.info("Обновлён пользователь: {}", updated);
        return updated;
    }

    public User getById(int id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: id=" + id));
    }

    public List<User> getAll() {
        return userStorage.getAll().stream().toList();
    }

    public void addFriend(int id, int friendId) {
        if (id == friendId) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        User u1 = getById(id);
        User u2 = getById(friendId);

        u1.getFriends().add(friendId);
        u2.getFriends().add(id);

        userStorage.update(u1);
        userStorage.update(u2);

        log.info("Пользователи {} и {} стали друзьями", id, friendId);
    }

    public void removeFriend(int id, int friendId) {
        User u1 = getById(id);
        User u2 = getById(friendId);

        u1.getFriends().remove(friendId);
        u2.getFriends().remove(id);

        userStorage.update(u1);
        userStorage.update(u2);

        log.info("Пользователи {} и {} больше не друзья", id, friendId);
    }

    public List<User> getFriends(int id) {
        User user = getById(id);
        return user.getFriends().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User u1 = getById(id);
        User u2 = getById(otherId);
        Set<Integer> common = u1.getFriends().stream()
                .filter(u2.getFriends()::contains)
                .collect(Collectors.toSet());
        return common.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }
    
    public boolean exists(int id) {
        return getById(id) != null;
    }
}