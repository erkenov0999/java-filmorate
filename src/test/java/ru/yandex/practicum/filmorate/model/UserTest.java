package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Arrange
        user = new User("test@email.com", "testLogin", "Test User", LocalDate.of(1990, 1, 1));
    }

    @Test
    void constructor_WithValidParameters_ShouldCreateUser() {
        // Arrange
        String email = "test@email.com";
        String login = "testLogin";
        String name = "Test User";
        LocalDate birthday = LocalDate.of(1990, 1, 1);

        // Act
        User newUser = new User(email, login, name, birthday);

        // Assert
        assertEquals(email, newUser.getEmail());
        assertEquals(login, newUser.getLogin());
        assertEquals(name, newUser.getName());
        assertEquals(birthday, newUser.getBirthday());
        assertNotNull(newUser.getFriends());
        assertTrue(newUser.getFriends().isEmpty());
    }

    @Test
    void setId_WithValidId_ShouldSetId() {
        // Arrange
        Long id = 1L;

        // Act
        user.setId(id);

        // Assert
        assertEquals(id, user.getId());
    }

    @Test
    void setEmail_WithValidEmail_ShouldSetEmail() {
        // Arrange
        String email = "new@email.com";

        // Act
        user.setEmail(email);

        // Assert
        assertEquals(email, user.getEmail());
    }

    @Test
    void setLogin_WithValidLogin_ShouldSetLogin() {
        // Arrange
        String login = "newLogin";

        // Act
        user.setLogin(login);

        // Assert
        assertEquals(login, user.getLogin());
    }

    @Test
    void setName_WithValidName_ShouldSetName() {
        // Arrange
        String name = "New Name";

        // Act
        user.setName(name);

        // Assert
        assertEquals(name, user.getName());
    }

    @Test
    void setBirthday_WithValidBirthday_ShouldSetBirthday() {
        // Arrange
        LocalDate birthday = LocalDate.of(1995, 5, 15);

        // Act
        user.setBirthday(birthday);

        // Assert
        assertEquals(birthday, user.getBirthday());
    }

    @Test
    void getFriends_ShouldReturnEmptySet() {
        // Arrange
        // (Инициализация в setUp)

        // Act
        Set<Long> friends = user.getFriends();

        // Assert
        assertNotNull(friends);
        assertTrue(friends.isEmpty());
    }

    @Test
    void getFriends_AfterAddingFriend_ShouldContainFriend() {
        // Arrange
        Long friendId = 2L;

        // Act
        user.getFriends().add(friendId);

        // Assert
        assertTrue(user.getFriends().contains(friendId));
        assertEquals(1, user.getFriends().size());
    }
}
