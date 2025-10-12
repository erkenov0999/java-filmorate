package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    UserDbStorage userStorage;


    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addNewFriend(long userId, long friendId) throws ResponseStatusException {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        checkingBeforeAddingFriends(userId, friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        log.info("Пользователи {} и {} добавили друг друга в друзья", friendId, userId);

        System.out.println("Пользователи " + user.getName() + " и "
                + friend.getName() + " добавили друг друга в друзья");
    }

    public void deleteFromFriends(long id, long friendId) throws ResponseStatusException {
        checkingBeforeAddingFriends(id, friendId);

        User user= userStorage.findUserById(id);
        User friend = userStorage.findUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(user.getId());
        userStorage.updateUser(user);

        log.info("Пользователь {} удалил из друзей {}", friendId, user.getId());

        System.out.println("Пользователи " + user.getName() + " и "
                + friend.getName() + " удалили друг друга из друзей");
    }

    public Set<User> getFriends(long userId) throws ResponseStatusException {
        if (userStorage.findUserById(userId) == null) {
            log.info("Пользователь с переданным ID={} не найден", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с переданным ID не найден");
        }

        User user = userStorage.findUserById(userId);

        Set<User> friends = new HashSet<>();
        for (long friendId : user.getFriends()) {
            friends.add(userStorage.findUserById(friendId));
        }

        return friends;
    }

    public Set<User> getCommonFriends(long firstUserId, long secondUserId) throws ResponseStatusException {
        checkingBeforeAddingFriends(firstUserId, secondUserId);

        User firstUser = userStorage.findUserById(firstUserId);
        User secondUser = userStorage.findUserById(secondUserId);

        Set<Long> commonFriends = new HashSet<>(secondUser.getFriends());
        commonFriends.retainAll(firstUser.getFriends());

        Set<User> friends = new HashSet<>();
        for (long friendId : commonFriends) {
            friends.add(userStorage.findUserById(friendId));
        }
        log.info("Вывод списка общих друзей между пользователям {} и пользователем {}", firstUserId, secondUserId);

        return friends;
    }

    private void checkingBeforeAddingFriends(long userId, long friendId) throws ResponseStatusException {
        if (userStorage.findUserById(userId) == null) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + userId + " для добавления/удаления друга.");
        }

        if (userStorage.findUserById(friendId) == null) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", friendId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + friendId + " для добавления/удаления друга.");
        }

        if (userId == friendId) {
            log.error("Запрещено добавлять в друзья или удалять самого себя {}", friendId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Запрещено выполнять это действие с самим собой.");
        }
    }
}