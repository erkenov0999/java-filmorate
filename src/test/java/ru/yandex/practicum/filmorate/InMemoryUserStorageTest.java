package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
