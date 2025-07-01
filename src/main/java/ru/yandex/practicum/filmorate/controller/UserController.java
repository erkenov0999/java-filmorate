package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    InMemoryUserStorage userStorage;

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
}