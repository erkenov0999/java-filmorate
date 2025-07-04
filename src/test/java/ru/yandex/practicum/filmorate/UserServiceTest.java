//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.UserService;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class UserServiceTest {
//    private final UserService userService = new UserService();
//
//    @Test
//    @DisplayName("Если имя пользователя null, то должно быть заменено на логин")
//    void whenNameIsNull_thenSetLoginAsName() {
//        //Arrange
//        User user = new User("validemail@mail.ru", "validlogin", null,
//                LocalDate.of(1996, 9, 29));
//
//        //Act
//        User result = userService.checkAndFillName(user);
//
//        //Assert
//        assertEquals(user.getLogin(), result.getName(), "Имя должно быть равно логину, когда имя null");
//    }
//
//    @Test
//    @DisplayName("Если имя пользователя пустое, то должно быть заменено на логин")
//    void whenNameIsBlank_thenSetLoginAsName() {
//        //Arrange
//        User user = new User("validemail@mail.ru", "validlogin", " ",
//                LocalDate.of(1996, 9, 29));
//
//        //Act
//        User result = userService.checkAndFillName(user);
//
//        //Assert
//        assertEquals(user.getLogin(), result.getName(), "Имя должно быть равно логину, когда имя пустое");
//    }
//
//    @Test
//    @DisplayName("Если имя пользователя указано, оно должно остаться неизменным")
//    void whenNameIsProvided_thenKeepNameUnchanged() {
//        //Arrange
//        User user = new User("validemail@mail.ru", "validlogin", "Name",
//                LocalDate.of(1996, 9, 29));
//
//        //Act
//        User result = userService.checkAndFillName(user);
//
//        //Assert
//        assertEquals("Name", result.getName(), "Имя должно остаться неизменным, когда оно указано");
//    }
//
//    @Test
//    @DisplayName("Генерация ID должна возвращать уникальные значения")
//    void generateId_shouldReturnIncrementedValues() {
//        //Arrange
//        long firstId = userService.generateId();
//        long secondId = userService.generateId();
//
//        //Assert
//        assertEquals(firstId + 1, secondId, "Каждый новый ID должен быть на 1 больше предыдущего");
//    }
//}
