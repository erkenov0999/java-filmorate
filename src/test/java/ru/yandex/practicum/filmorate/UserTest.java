package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Правильно созданный пользователь должен проходить валидацию")
    void validUser_MustPassVerification() {
        // Arrange
        User user = new User("validemail@mail.ru", "validlogin", "Name",
                LocalDate.of(1996, 9, 29));

        //Act
        var violations = validator.validate(user);

        //Assert
        assertTrue(violations.isEmpty(), "Для валидного пользователя, не должно быть ошибок");
    }

    @Test
    @DisplayName("При не правильно введеной почте, валидация должна не проходить")
    void incorrectMail_ShouldFailValidation() {
        //Arrange
        User user = new User("failvalidemail.ru", "validlogin", "Name",
                LocalDate.of(1996, 9, 29));

        //Act
        var violations = validator.validate(user);

        //Assert
        assertEquals(1, violations.size(), "Не корректная почта должна выдать ошибку");
    }

    @Test
    @DisplayName("Не введенная почта не должна проходить валидацию")
    void anEmptyEmail_MustFailValidation() {
        //Arrange
        User user = new User(null, "validlogin", "Name",
                LocalDate.of(1996, 9, 29));

        //Act
        var violations = validator.validate(user);

        //Assert
        assertEquals(1, violations.size(), "Не введенная почта должна выдать ошибку");
    }

    @Test
    @DisplayName("Пустой логин должен не пройти валидацию")
    void anEmptyLogin_MustFailValidation() {
        //Arrange
        User user = new User("validemail@mail.ru", null, "Name",
                LocalDate.of(1996, 9, 29));

        //Act
        var violations = validator.validate(user);

        //Assert
        assertEquals(1, violations.size(), "Не введенная почта должна выдать ошибку");
    }

    @Test
    @DisplayName("Логин с пробелом  должен не пройти валидацию")
    void usernameWithASpace_MustFailValidation() {
        //Arrange
        User user = new User("validemail@mail.ru", "fail login", "Name",
                LocalDate.of(1996, 9, 29));

        //Act
        var violations = validator.validate(user);

        //Assert
        assertEquals(1, violations.size(), "Не введенная почта должна выдать ошибку");
    }

    @Test
    @DisplayName("Пустое имя должно пройти валидацию")
    void anEmptyName_IsBeingValidated() {
        // Arrange
        User user = new User("validemail@mail.ru", "validlogin", null,
                LocalDate.of(1996, 9, 29));

        //Act
        var violations = validator.validate(user);

        //Assert
        assertTrue(violations.isEmpty(), "Для пустого имени, валидация должна проходить");
    }

    @Test
    @DisplayName("При пустой дате рождения, валидация должна не пройти")
    void anEmptyBirthday_IsNotBeingValidated() {
        //Arrange
        User user = new User("validemail@mail.ru", "login", "Name", null);

        //Act
        var violations = validator.validate(user);

        //Assert
        assertEquals(1, violations.size(), "Пустая дата рождения должна выдать ошибку");
    }

    @Test
    @DisplayName("Дата рождения в будущем должна вызывать ошибку валидации")
    void futureBirthday_ShouldFailValidation() {
        // Arrange
        User user = new User("valid@email.com", "login", "Name", LocalDate.now().plusDays(1));

        // Act
        var violations = validator.validate(user);

        // Assert
        assertEquals(1, violations.size(), "Должны быть нарушения валидации для даты рождения в будущем");
    }
}
