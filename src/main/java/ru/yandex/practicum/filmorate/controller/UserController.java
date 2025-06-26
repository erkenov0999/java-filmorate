package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public void getAllUsers() {
        userStorage.getAllUsers();
    }
}