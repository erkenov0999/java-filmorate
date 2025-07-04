package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    InMemoryUserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

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
    public void getAllUsers() {
        log.info("Получения списка всех пользователей");
        userStorage.getAllUsers();
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addNewFriend(@Valid @RequestBody User user, @PathVariable int friendId) {
        log.info("Пользователь {} добавил нового друга", user);
        userService.addNewFriend(user, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@Valid @RequestBody User user, @PathVariable int friendId) {
        log.info("Пользователь {} удалил друга", user);
        userService.deleteFromFriends(user, friendId);
    }

    @GetMapping("{id}/friends")
    public void getFriends(@PathVariable int id) {
        log.info("Список друзей пользователя с ID {}", id);
        userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public void getFriendsCommon(@PathVariable int otherId, @PathVariable int id) {
        log.info("Список общих друзей между пользователем ID-{} и пользователем ID-{}", otherId, id);
        userService.checkCommonFriends(otherId, id);
    }
}