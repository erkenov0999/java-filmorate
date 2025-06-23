package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private User testUser;

    @BeforeEach
    void setUp() {
        UserService userService = new UserService();
        userController = new UserController(userService);

        testUser = new User("user@email.com", "login", "Name",
                LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("При добавлении пользователя возвращается корректный пользователь")
    void addNewUser_returns_correct_user() {
        //Arrange & Act
        User result = userController.addNewUser(testUser);

        //Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getLogin(), result.getLogin());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getBirthday(), result.getBirthday());
    }

    @Test
    @DisplayName("При обновлении пользователя данные изменяются корректно")
    void updateUser_changes_user_data_correctly() {
        //Arrange
        User addedUser = userController.addNewUser(testUser);

        //Act
        User updatedUser = new User(
                "updated@email.com",
                "updatedLogin",
                "Updated Name",
                LocalDate.of(1995, 1, 1)
        );
        updatedUser.setId(addedUser.getId());

        User result = userController.updateUser(updatedUser);

        //Assert
        assertNotNull(result);
        assertEquals(addedUser.getId(), result.getId());
        assertEquals("updated@email.com", result.getEmail());
        assertEquals("updatedLogin", result.getLogin());
        assertEquals("Updated Name", result.getName());
        assertEquals(LocalDate.of(1995, 1, 1), result.getBirthday());
    }

    @Test
    @DisplayName("При обновлении несуществующего пользователя выбрасывается исключение")
    void updateUser_throws_exception_when_user_not_found() {
        //Arrange
        testUser.setId(999L);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.updateUser(testUser));

        assertEquals("Пользователь с ID 999 не найден", exception.getMessage());
    }

    @Test
    @DisplayName("При отсутствии пользователей возвращается пустой список")
    void getAllUsers_returns_empty_list_when_no_users() {
        //Arrange & Act
        List<User> result = userController.getAllUsers();

        //Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("При наличии пользователей возвращается их список")
    void getAllUsers_returns_all_users_when_users_exist() {
        //Arrange
        userController.addNewUser(testUser);

        //Act
        List<User> result = userController.getAllUsers();

        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getEmail(), result.get(0).getEmail());
        assertEquals(testUser.getLogin(), result.get(0).getLogin());
    }

    @Test
    @DisplayName("При пустом имени оно заменяется логином")
    void addNewUser_sets_login_as_name_when_name_is_empty() {
        //Arrange
        User userWithoutName = new User(
                "user@email.com",
                "login",
                "",
                LocalDate.of(1990, 1, 1)
        );

        //Act
        User result = userController.addNewUser(userWithoutName);

        //Assert
        assertNotNull(result);
        assertEquals("login", result.getName());
    }

    @Test
    @DisplayName("При имени из пробелов оно заменяется логином")
    void addNewUser_sets_login_as_name_when_name_is_blank() {
        //Arrange
        User userWithBlankName = new User(
                "user@email.com",
                "login",
                "   ",
                LocalDate.of(1990, 1, 1)
        );

        //Act
        User result = userController.addNewUser(userWithBlankName);

        //Assert
        assertNotNull(result);
        assertEquals("login", result.getName());
    }
}
