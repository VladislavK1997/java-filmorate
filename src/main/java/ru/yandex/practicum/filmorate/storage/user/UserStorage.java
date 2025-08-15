package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUser(Long id);  // Изменено на Optional

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();
}


