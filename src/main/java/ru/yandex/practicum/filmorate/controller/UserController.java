package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private Map<Long, User> users = new HashMap<>();

    @PostMapping("/user")
    public User addNewUser(@RequestBody User user) {
        users.put(user.getId(), user);
        System.out.println("Пользователь под ником " + user.getName() + " удачно зарегистрирован!");

        log.info("Добавлен новый пользователь {}", user.toString());
        return user;
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            System.out.println("Пользователь под ником " + user.getName() + " удачно обновлен!");
            log.info("Пользователь {} обновлен", user.toString());
        } else {
            System.out.println("Ошибка при обновлении данных пользователя "
                    + user.getName() + " проверьте корректность данных");
            log.info("Ошибка при обновлении данных пользователя {}", user.toString());
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }
}
