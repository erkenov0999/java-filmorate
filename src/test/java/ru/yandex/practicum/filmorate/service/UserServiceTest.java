package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    private UserService userService;
    private UserStorage userStorage;
    private FriendsService friendsService;

    private User testUser;
    private User testFriend;

    @BeforeEach
    void setUp() {
        // Arrange
        userStorage = null; // В реальном тесте будет зависимость
        friendsService = null; // В реальном тесте будет зависимость
        userService = new UserService(userStorage, friendsService);

        testUser = new User("test@email.com", "testLogin", "Test User", LocalDate.of(1990, 1, 1));
        testUser.setId(1L);
        testFriend = new User("friend@email.com", "friendLogin", "Friend User", LocalDate.of(1992, 1, 1));
        testFriend.setId(2L);
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
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(testUser);
        assertEquals("test@email.com", testUser.getEmail());
        assertEquals("testLogin", testUser.getLogin());
        assertEquals("Test User", testUser.getName());
    }

    @Test
    void testFriend_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(testFriend);
        assertEquals("friend@email.com", testFriend.getEmail());
        assertEquals("friendLogin", testFriend.getLogin());
        assertEquals("Friend User", testFriend.getName());
    }

    @Test
    void checkAndFillName_WhenNameIsNull_ShouldSetLoginAsName() {
        // Arrange
        User user = new User("test@email.com", "testLogin", null, LocalDate.of(1990, 1, 1));

        // Act
        User result = userService.checkAndFillName(user);

        // Assert
        assertEquals("testLogin", result.getName());
    }

    @Test
    void checkAndFillName_WhenNameIsBlank_ShouldSetLoginAsName() {
        // Arrange
        User user = new User("test@email.com", "testLogin", "   ", LocalDate.of(1990, 1, 1));

        // Act
        User result = userService.checkAndFillName(user);

        // Assert
        assertEquals("testLogin", result.getName());
    }

    @Test
    void checkAndFillName_WhenNameIsEmpty_ShouldSetLoginAsName() {
        // Arrange
        User user = new User("test@email.com", "testLogin", "", LocalDate.of(1990, 1, 1));

        // Act
        User result = userService.checkAndFillName(user);

        // Assert
        assertEquals("testLogin", result.getName());
    }

    @Test
    void checkAndFillName_WhenNameIsNotEmpty_ShouldKeepName() {
        // Arrange
        User user = new User("test@email.com", "testLogin", "Test Name", LocalDate.of(1990, 1, 1));

        // Act
        User result = userService.checkAndFillName(user);

        // Assert
        assertEquals("Test Name", result.getName());
    }
}
