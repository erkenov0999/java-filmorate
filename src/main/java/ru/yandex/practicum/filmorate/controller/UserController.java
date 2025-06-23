package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    private final UserService userService;

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        User checked = userService.checkAndFillName(user);

        checked.setId(userService.generateId());

        users.put(checked.getId(), checked);
        log.info("Добавлен новый пользователь: {}", checked);
        return checked;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        Long id = user.getId();

        if (id == null || !users.containsKey(id)) {
            throw new ValidationException("Пользователь с ID " + id + " не найден");
        }

        User checked = userService.checkAndFillName(user);

        users.put(checked.getId(), checked);
        log.info("Обновлен пользователь: {}", checked);
        return checked;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}