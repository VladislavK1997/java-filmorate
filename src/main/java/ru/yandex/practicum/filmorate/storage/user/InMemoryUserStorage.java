package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public boolean exists(Long id) {
        return users.containsKey(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(idCounter++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().put(friendId, User.FriendshipStatus.PENDING);
        friend.getFriends().put(userId, User.FriendshipStatus.PENDING);
    }

    @Override
    public void confirmFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().put(friendId, User.FriendshipStatus.CONFIRMED);
        friend.getFriends().put(userId, User.FriendshipStatus.CONFIRMED);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> getFriends(Long id) {
        User user = users.get(id);
        if (user == null) {
            return Collections.emptyList();
        }

        return user.getFriends().keySet().stream()
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getConfirmedFriends(Long id) {
        User user = users.get(id);
        if (user == null) {
            return Collections.emptyList();
        }

        return user.getFriends().entrySet().stream()
                .filter(entry -> entry.getValue() == User.FriendshipStatus.CONFIRMED)
                .map(Map.Entry::getKey)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getPendingFriends(Long id) {
        User user = users.get(id);
        if (user == null) {
            return Collections.emptyList();
        }

        return user.getFriends().entrySet().stream()
                .filter(entry -> entry.getValue() == User.FriendshipStatus.PENDING)
                .map(Map.Entry::getKey)
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        User user1 = users.get(id);
        User user2 = users.get(otherId);

        if (user1 == null || user2 == null) {
            return Collections.emptyList();
        }

        Set<Long> user1Friends = user1.getFriends().entrySet().stream()
                .filter(entry -> entry.getValue() == User.FriendshipStatus.CONFIRMED)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Set<Long> user2Friends = user2.getFriends().entrySet().stream()
                .filter(entry -> entry.getValue() == User.FriendshipStatus.CONFIRMED)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Set<Long> commonFriendIds = new HashSet<>(user1Friends);
        commonFriendIds.retainAll(user2Friends);

        return commonFriendIds.stream()
                .map(this::getUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}