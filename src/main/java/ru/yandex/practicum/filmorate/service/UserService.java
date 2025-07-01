package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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

    public void addNewFriend(User user, User newFriend) {
        checkingBeforeAddingFriends(user, newFriend);

        user.getFriends().add(newFriend.getId());
        newFriend.getFriends().add(user.getId());
        System.out.println("Пользователи " + user.getName() + " и "
                + newFriend.getName() + " добавили друг друга в друзья");
    }

    public void deleteFromFriends(User user, User friend) {
        checkingBeforeAddingFriends(user, friend);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        System.out.println("Пользователи " + user.getName() + " и "
                + friend.getName() + " удалили друг друга из друзей");
    }

    public Set<Long> checkCommonFriends(User firstUser, User secondUser) {
        Set<Long> commonFriends = firstUser.getFriends().stream()
                .filter(secondUser.getFriends()::contains)
                .collect(Collectors.toSet());
        return commonFriends;
    }

    private void checkingBeforeAddingFriends(User user, User newFriend) {
        Long userId = userStorage.getUserById(user).getId();
        Long newFriendId = userStorage.getUserById(newFriend).getId();

        if (userId == null) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", user.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + user.getId() + " для добавления/удаления друга.");
        }

        if (newFriendId == null) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", newFriend.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + newFriend.getId() + " для добавления/удаления друга.");
        }

        if (user.equals(newFriend)) {
            log.error("Запрещено добавлять в друзья или удалять самого себя {}", newFriend.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Запрещено выполнять это действие с самим собой.");
        }
    }
}