package ru.yandex.practicum.filmorate.model;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User checkAndFillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}