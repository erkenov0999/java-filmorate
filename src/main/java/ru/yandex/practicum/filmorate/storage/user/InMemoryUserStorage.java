package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    UserService userService;


    @Override
    public User addNewUser(@Valid @RequestBody User user) {
        User checked = userService.checkAndFillName(user);

        checked.setId(userService.generateId());

        users.put(checked.getId(), checked);
        log.info("Добавлен новый пользователь: {}", checked);
        return checked;
    }

    @Override
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

    @Override
    public void deleteUser(User user) {
        Long id = user.getId();

        if (id == null || !users.containsKey(id)) {
            throw new ValidationException("Пользователь \"" + user.getName() + "\" не найден");
        }

        users.remove(id);
        log.info("Удален пользователь: {}", user.getName());
        System.out.println("Пользователь \"" + user.getName() + "\" удален");
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(User user) {
        long id = user.getId();
        return users.get(id);
    }
}
