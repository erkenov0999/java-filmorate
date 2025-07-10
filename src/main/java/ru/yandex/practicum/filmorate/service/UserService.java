package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    InMemoryUserStorage userStorage;


    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addNewFriend(long userId, long friendId) throws ResponseStatusException {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        checkingBeforeAddingFriends(user, friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        //userStorage.updateUser(user);
        //userStorage.updateUser(friend);

        log.info("Пользователи {} и {} добавили друг друга в друзья", friendId, userId);

        System.out.println("Пользователи " + user.getName() + " и "
                + friend.getName() + " добавили друг друга в друзья");
    }

    public void deleteFromFriends(long id, long friendId) {
        User user= userStorage.getUserById(id);
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

    public Set<Long> getCommonFriends(long firstUserId, long secondUserId) {
        User firstUser = userStorage.getUserById(firstUserId);
        User secondUser = userStorage.getUserById(secondUserId);

        Set<Long> commonFriends = new HashSet<>(firstUser.getFriends());
        commonFriends.retainAll(secondUser.getFriends());
        log.info("Вывод списка общих друзей между пользователям {} и пользователем {}", firstUserId, secondUserId);

        return commonFriends;
    }

    private void checkingBeforeAddingFriends(User user, long friendId) {
        long userId = user.getId();

        if (userStorage.getUserById(userId) == null) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + user.getId() + " для добавления/удаления друга.");
        }

        if (userStorage.getUserById(friendId) == null) {
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