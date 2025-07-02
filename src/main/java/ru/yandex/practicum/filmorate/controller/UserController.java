package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping
    public void addNewUser(@Valid @RequestBody User user) {
        userStorage.addNewUser(user);
    }

    @PutMapping
    public void updateUser(@Valid @RequestBody User user) {
        userStorage.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(@Valid @RequestBody User user) {
        userStorage.deleteUser(user);
    }

    @GetMapping
    public void getAllUsers() {
        userStorage.getAllUsers();
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addNewFriend(@Valid @RequestBody User user, @PathVariable int friendId) {
        userService.addNewFriend(user, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@Valid @RequestBody User user, @PathVariable int friendId) {
        userService.deleteFromFriends(user, friendId);
    }

    @GetMapping("{id}/friends")
    public void getFriends(@PathVariable int id) {
        userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public void getFriendsCommon(@PathVariable int otherId, @PathVariable int id) {
        userService.checkCommonFriends(otherId, id);
    }
}