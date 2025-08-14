package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);
    User update(User user);
    Optional<User> getById(int id);
    Collection<User> getAll();

    default boolean existsById(int id) {
        return getById(id).isPresent();
    }
}
