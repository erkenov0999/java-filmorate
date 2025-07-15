package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserStorageTest {
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    @Test
    @DisplayName("Если имя пользователя null, то должно быть заменено на логин")
    void whenNameIsNull_thenSetLoginAsName() {
        //Arrange
        User user = new User("validemail@mail.ru", "validlogin", null,
                LocalDate.of(1996, 9, 29));

        //Act
        User result = userStorage.checkAndFillName(user);

        //Assert
        assertEquals(user.getLogin(), result.getName(), "Имя должно быть равно логину, когда имя null");
    }

    @Test
    @DisplayName("Если имя пользователя пустое, то должно быть заменено на логин")
    void whenNameIsBlank_thenSetLoginAsName() {
        //Arrange
        User user = new User("validemail@mail.ru", "validlogin", " ",
                LocalDate.of(1996, 9, 29));

        //Act
        User result = userStorage.checkAndFillName(user);

        //Assert
        assertEquals(user.getLogin(), result.getName(), "Имя должно быть равно логину, когда имя пустое");
    }

    @Test
    @DisplayName("Если имя пользователя указано, оно должно остаться неизменным")
    void whenNameIsProvided_thenKeepNameUnchanged() {
        //Arrange
        User user = new User("validemail@mail.ru", "validlogin", "Name",
                LocalDate.of(1996, 9, 29));

        //Act
        User result = userStorage.checkAndFillName(user);

        //Assert
        assertEquals("Name", result.getName(), "Имя должно остаться неизменным, когда оно указано");
    }

    @Test
    @DisplayName("Генерация ID должна возвращать уникальные значения")
    void generateId_shouldReturnIncrementedValues() {
        //Arrange
        long firstId = userStorage.generateId();
        long secondId = userStorage.generateId();

        //Assert
        assertEquals(firstId + 1, secondId, "Каждый новый ID должен быть на 1 больше предыдущего");
    }

    @Test
    @DisplayName("Добавление нового пользователя")
    void add_new_user_test() {
        //Arrange
        User user = new User("validemail@mail.ru", "validlogin", "Islam",
                LocalDate.of(1996, 9, 29));

        //Act
        User result = userStorage.addNewUser(user);

        //Assert
        assertEquals(user, result, "Созданый пользователь должен быть эдентично переданным данным");
        assertEquals(result, userStorage.getUsers().get(result.getId()),
                "Созданный пользователь должен появится в хранилище пользователей");

    }

    @Test
    @DisplayName("Обновление данных пользователя, после обновления должен возвращать изменный вариант пользователя")
    void update_user_test() {
        //Arrange
        User user = new User("validemail@mail.ru", "validlogin", "Islam",
                LocalDate.of(1996, 9, 29));
        userStorage.addNewUser(user);

        //Act
        user.setName("Petya");
        userStorage.updateUser(user);

        //Assert
        assertEquals(user, userStorage.getUsers().get(user.getId()), "Пользователь обновлен и перезаписан в хранилище");
    }

    @Test
    @DisplayName("При удалении пользователя, он должен удалиться из хранилища")
    void delete_user_test() {
        //Arrange
        User user = new User("validemail@mail.ru", "validlogin", "Islam",
                LocalDate.of(1996, 9, 29));
        userStorage.addNewUser(user);
        Long userId = user.getId();

        //Act
        userStorage.deleteUser(user);

        //Assert
        assertNull(userStorage.getUsers().get(userId), "Удаленныый пользователь отсутсвует в хранилище");
    }

    @Test
    @DisplayName("Получение всех пользователей должно возвращать всех добавленных пользователей")
    void getAllUsers_shouldReturnAllAddedUsers() {
        // Arrange
        User user1 = new User("user1@mail.ru", "user1", "User1", LocalDate.of(1990, 1, 1));
        User user2 = new User("user2@mail.ru", "user2", "User2", LocalDate.of(1990, 1, 1));
        userStorage.addNewUser(user1);
        userStorage.addNewUser(user2);

        // Act
        List<User> allUsers = userStorage.getAllUsers();

        // Assert
        assertEquals(2, allUsers.size(), "Должны быть возвращены все добавленные пользователи");
        assertTrue(allUsers.contains(user1) && allUsers.contains(user2),
                "Возвращенный список должен содержать всех добавленных пользователей");
    }

    @Test
    @DisplayName("Получение пользователя по ID должно возвращать корректного пользователя")
    void getUserById_shouldReturnCorrectUser() {
        // Arrange
        User user = new User("user@mail.ru", "user", "User", LocalDate.of(1990, 1, 1));
        userStorage.addNewUser(user);
        Long userId = user.getId();

        // Act
        User foundUser = userStorage.getUserById(userId);

        // Assert
        assertEquals(user, foundUser, "Найденный пользователь должен соответствовать добавленному");
    }

    @Test
    @DisplayName("Получение несуществующего пользователя по ID должно возвращать null")
    void getNonExistentUserById_shouldReturnNull() {
        // Act
        User foundUser = userStorage.getUserById(999L);

        // Assert
        assertNull(foundUser, "Для несуществующего ID должен возвращаться null");
    }

    @Test
    @DisplayName("Обновление несуществующего пользователя должно вызывать исключение")
    void updateNonExistentUser_shouldThrowException() {
        // Arrange
        User user = new User("user@mail.ru", "user", "User", LocalDate.of(1990, 1, 1));
        user.setId(999L);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> userStorage.updateUser(user),
                "Должно быть выброшено исключение при попытке обновить несуществующего пользователя");
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя должно вызывать исключение")
    void deleteNonExistentUser_shouldThrowException() {
        // Arrange
        User user = new User("user@mail.ru", "user", "User", LocalDate.of(1990, 1, 1));
        user.setId(999L);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> userStorage.deleteUser(user),
                "Должно быть выброшено исключение при попытке удалить несуществующего пользователя");
    }
}
