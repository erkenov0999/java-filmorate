package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    private long idCounter = 1;


    public Long generateId() {
        return idCounter++;
    }

    @Override
    public User addNewUser(User user) {
        User checked = checkAndFillName(user);

        checked.setId(generateId());

        users.put(checked.getId(), checked);
        log.info("Добавлен новый пользователь: {}", checked);
        return checked;
    }

    @Override
    public User updateUser(User user) throws ResponseStatusException {
        Long id = user.getId();

        if (id == null || !users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + id + " не найден");
        }

        User checked = checkAndFillName(user);

        users.put(checked.getId(), checked);
        log.info("Обновлен пользователь: {}", checked);
        return checked;
    }

    @Override
    public void deleteUser(User user) throws ResponseStatusException {
        Long id = user.getId();

        if (id == null || !users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь \"" + user.getName() + "\" не найден");
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
    public User getUserById(long id) {
        return users.get(id);
    }

    public User checkAndFillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}
