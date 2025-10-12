package ru.yandex.practicum.filmorate.storage.user.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addNewUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    List<User> findAllUsers();
    User findUserById(long id);
}
