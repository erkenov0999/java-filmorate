package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserDbStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("Создание нового пользователя: {}", user);
        return userStorage.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновление данных пользователя: {}", user);
        return userStorage.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(@Valid @RequestBody User user) {
        log.info("Удаление пользователя: {}", user);
        userStorage.deleteUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получения списка всех пользователей");
        return userStorage.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("Получение пользователя с ID: {}", id);
        return userStorage.findUserById(id).orElse(null);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь {} добавил нового друга", id);
        userService.addNewFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь {} удалил друга", id);
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable long id) {
        log.info("Список друзей пользователя с ID {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getFriendsCommon(@PathVariable long otherId, @PathVariable long id) {
        log.info("Список общих друзей между пользователем ID-{} и пользователем ID-{}", otherId, id);
        return userService.getCommonFriends(otherId, id);
    }
}