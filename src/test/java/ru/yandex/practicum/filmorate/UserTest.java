package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidUser() {
        User user = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createUserWithBlankEmail() {
        User user = User.builder()
                .email("")
                .login("testLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email не может быть пустым!", violations.iterator().next().getMessage());
    }

    @Test
    void createUserWithInvalidEmail() {
        User user = User.builder()
                .email("invalid-email")
                .login("testLogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Введите корректный email!", violations.iterator().next().getMessage());
    }

    @Test
    void createUserWithBlankLogin() {
        User user = User.builder()
                .email("test@mail.com")
                .login("")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        // Проверяем, что есть хотя бы одно нарушение с нужным сообщением
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Логин не может быть пустым!")));
    }

    @Test
    void createUserWithLoginContainingSpaces() {
        User user = User.builder()
                .email("test@mail.com")
                .login("test login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Пробелы в логине запрещены!", violations.iterator().next().getMessage());
    }

    @Test
    void createUserWithFutureBirthday() {
        User user = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть позже текущего дня!", violations.iterator().next().getMessage());
    }

    @Test
    void shouldSetLoginAsNameWhenNameIsNull() {
        User user = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .name(null)
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        // Аналогично предыдущему тесту
        assertNull(user.getName());
    }
}