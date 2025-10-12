package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    private UserDbStorage userStorage;

    @BeforeEach
    void setUp() {
        //userStorage = new UserDbStorage();
        userService = new UserService(userStorage);
    }

    @Test
    @DisplayName("Добавление друга должно добавить ID в друзья обоим пользователям")
    void addNewFriend_shouldAddFriendToBothUsers() {
        // Arrange
        User firstUser = new User("firstUser@mail.ru", "firstUser", "Vanya", LocalDate.of(1990, 1, 1));
        User secondUser = new User("secondUser@mail.ru", "secondUser", "Petya", LocalDate.of(1995, 5, 5));
        userStorage.addNewUser(firstUser);
        userStorage.addNewUser(secondUser);

        // Act
        userService.addNewFriend(firstUser.getId(), secondUser.getId());

        // Assert
        assertTrue(firstUser.getFriends().contains(secondUser.getId()), "Друг должен быть добавлен в список друзей пользователя");
        assertTrue(secondUser.getFriends().contains(firstUser.getId()), "Пользователь должен быть добавлен в список друзей друга");
    }

    @Test
    @DisplayName("Попытка добавить несуществующего пользователя в друзья должна вызывать исключение")
    void addNewFriend_withNonExistentUser_shouldThrowException() {
        // Arrange
        User user = new User("firstUser@mail.ru", "firstUser", "Vanya", LocalDate.of(1990, 1, 1));
        userStorage.addNewUser(user);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> userService.addNewFriend(user.getId(), 999L),
                "Должно быть выброшено исключение при попытке добавить несуществующего пользователя");
    }

    @Test
    @DisplayName("Удаление друга должно удалить ID из друзей обоих пользователей")
    void deleteFromFriends_shouldRemoveFriendFromBothUsers() {
        // Arrange
        User firstUser = new User("firstUser@mail.ru", "firstUser", "Vanya", LocalDate.of(1990, 1, 1));
        User secondUser = new User("secondUser@mail.ru", "secondUser", "Petya", LocalDate.of(1995, 5, 5));
        userStorage.addNewUser(firstUser);
        userStorage.addNewUser(secondUser);
        userService.addNewFriend(firstUser.getId(), secondUser.getId());

        // Act
        userService.deleteFromFriends(firstUser.getId(), secondUser.getId());

        // Assert
        assertFalse(firstUser.getFriends().contains(secondUser.getId()), "Друг должен быть удален из списка друзей пользователя");
        assertFalse(secondUser.getFriends().contains(firstUser.getId()), "Пользователь должен быть удален из списка друзей друга");
    }

    @Test
    @DisplayName("Получение списка друзей должно возвращать корректный набор пользователей")
    void getFriends_shouldReturnCorrectSetOfUsers() {
        // Arrange
        User firstUser = new User("firstUser@mail.ru", "firstUser", "Vanya", LocalDate.of(1990, 1, 1));
        User secondUser = new User("secondUser@mail.ru", "secondUser", "Petya", LocalDate.of(1995, 5, 5));
        userStorage.addNewUser(firstUser);
        userStorage.addNewUser(secondUser);
        userService.addNewFriend(firstUser.getId(), secondUser.getId());

        // Act
        Set<User> friends = userService.getFriends(firstUser.getId());

        // Assert
        assertEquals(1, friends.size(), "Должен быть возвращен один друг");
        assertTrue(friends.contains(secondUser), "Возвращенный друг должен соответствовать добавленному");
    }

    @Test
    @DisplayName("Получение общих друзей должно возвращать только общих друзей")
    void getCommonFriends_shouldReturnOnlyCommonFriends() {
        // Arrange
        User firstUser = new User("firstUser@mail.ru", "firstUser", "Vanya", LocalDate.of(1990, 1, 1));
        User secondUser = new User("secondUser@mail.ru", "secondUser", "Petya", LocalDate.of(1995, 5, 5));
        User commonFriend = new User("friend@mail.ru", "friend", "Friend", LocalDate.of(2000, 10, 10));
        userStorage.addNewUser(firstUser);
        userStorage.addNewUser(secondUser);
        userStorage.addNewUser(commonFriend);

        userService.addNewFriend(firstUser.getId(), commonFriend.getId());
        userService.addNewFriend(secondUser.getId(), commonFriend.getId());

        // Act
        Set<User> commonFriends = userService.getCommonFriends(firstUser.getId(), secondUser.getId());

        // Assert
        assertEquals(1, commonFriends.size(), "Должен быть возвращен один общий друг");
        assertTrue(commonFriends.contains(commonFriend), "Возвращенный друг должен быть общим для обоих пользователей");
    }

    @Test
    @DisplayName("Попытка добавить себя в друзья должна вызывать исключение")
    void addSelfAsFriend_shouldThrowException() {
        // Arrange
        User user = new User("firstUser@mail.ru", "firstUser", "Vanya", LocalDate.of(1990, 1, 1));
        userStorage.addNewUser(user);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> userService.addNewFriend(user.getId(), user.getId()),
                "Должно быть выброшено исключение при попытке добавить себя в друзья");
    }
}