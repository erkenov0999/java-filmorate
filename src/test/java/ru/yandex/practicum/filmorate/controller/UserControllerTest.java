package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Arrange
        userService = new UserService(null, null); // В реальном тесте будут зависимости
        userController = new UserController(userService);
    }

    @Test
    void userController_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(userController);
    }

    @Test
    void userService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(userService);
    }

    @Test
    void testUser_ShouldBeCreated() {
        // Arrange
        User user = new User("test@email.com", "testLogin", "Test User", LocalDate.of(1990, 1, 1));

        // Act & Assert
        assertNotNull(user);
        assertEquals("test@email.com", user.getEmail());
        assertEquals("testLogin", user.getLogin());
        assertEquals("Test User", user.getName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthday());
    }

    @Test
    void testUser_ShouldHaveEmptyFriends() {
        // Arrange
        User user = new User("test@email.com", "testLogin", "Test User", LocalDate.of(1990, 1, 1));

        // Act
        var friends = user.getFriends();

        // Assert
        assertNotNull(friends);
        assertTrue(friends.isEmpty());
    }

    @Test
    void testUser_ShouldHaveNullId() {
        // Arrange
        User user = new User("test@email.com", "testLogin", "Test User", LocalDate.of(1990, 1, 1));

        // Act & Assert
        assertNull(user.getId());
    }
}
