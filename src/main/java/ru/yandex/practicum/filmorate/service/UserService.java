package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    private long idCounter = 1;

    public User checkAndFillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }

    public Long generateId() {
        return idCounter++;
    }

    public void addNewFriend(User user, long friendId) {
        User friend = userStorage.getUserById(friendId);

        checkingBeforeAddingFriends(user, friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(user.getId());
        userStorage.updateUser(user);

        log.info("Пользователи {} и {} добавили друг друга в друзья", friendId, user.getId());

        System.out.println("Пользователи " + user.getName() + " и "
                + friend.getName() + " добавили друг друга в друзья");
    }

    public void deleteFromFriends(User user, long friendId) {
        User friend = userStorage.getUserById(friendId);

        checkingBeforeAddingFriends(user, friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(user.getId());
        userStorage.updateUser(user);

        log.info("Пользователь {} удалил из друзей {}", friendId, user.getId());

        System.out.println("Пользователи " + user.getName() + " и "
                + friend.getName() + " удалили друг друга из друзей");
    }

    public Set<User> getFriends(long userId) {
        User user = userStorage.getUserById(userId);

        Set<User> friends = new HashSet<>();
        for (long friendId : user.getFriends()) {
            friends.add(userStorage.getUserById(friendId));
        }

        return friends;
    }

    public Set<Long> checkCommonFriends(long firstUserId, long secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);

        return firstUser.getFriends().stream()
                .filter(secondUser.getFriends()::contains)
                .collect(Collectors.toSet());
    }

    private void checkingBeforeAddingFriends(User user, long friendId) {
        Long userId = userStorage.getUserById(user.getId()).getId();

        if (userId == null) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", user.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + user.getId() + " для добавления/удаления друга.");
        }

        if (friendId == 0) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", friendId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + friendId + " для добавления/удаления друга.");
        }

        if (userId.equals(friendId)) {
            log.error("Запрещено добавлять в друзья или удалять самого себя {}", friendId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Запрещено выполнять это действие с самим собой.");
        }
    }
}