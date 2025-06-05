package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void addNewValidUser() {
        User user = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User addedUser = userController.addNewUser(user);
        assertNotNull(addedUser.getId());
        assertEquals(1, userController.getAllUsers().size());
    }

    @Test
    void addNewUserWithBlankName() {
        User user = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .name("")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User addedUser = userController.addNewUser(user);
        assertEquals("testLogin", addedUser.getName());
    }

    @Test
    void updateUserWithInvalidId() {
        User user = User.builder()
                .id(999L)
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

    @Test
    void getAllUsers() {
        User user1 = User.builder()
                .email("test1@mail.com")
                .login("testLogin1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User user2 = User.builder()
                .email("test2@mail.com")
                .login("testLogin2")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        userController.addNewUser(user1);
        userController.addNewUser(user2);

        assertEquals(2, userController.getAllUsers().size());
    }
}