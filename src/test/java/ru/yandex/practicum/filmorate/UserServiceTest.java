package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

public class UserServiceTest {
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);


    @Test
    @DisplayName("Пользователь не сможет добавить в друзья не существующего пользователя")
    void add_new_friend_if_user_does_not_exist() {

    }
}
