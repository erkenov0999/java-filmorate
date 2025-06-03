package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * User.
 */

@Data
@Builder
public class User {

    private static long idCounter = 1; //статический счетчик для генерации id

    private long id; //id пользователя

    @NotBlank(message = "Email не может быть пустым!")
    @Email(message = "Введите корректный email!")
    private String email; //почта пользователя

    @NotBlank(message = "Логин не может быть пустым!")
    @Pattern(regexp = "^\\S+$", message = "Пробелы в логине запрещены!")
    private String login; //логин пользователя

    private String name; //никнейм пользователя

    @NotBlank(message = "Дата рождения не может быть пустой!")
    @PastOrPresent(message = "Дата рождения не может быть позже текущего дня!")
    private LocalDate birthday; //дата рождения пользователя

    public User createUser(String email, String login, String name, LocalDate birthday) {
        return User.builder()
                .id(idCounter++)
                .email(email)
                .login(login)
                .name(checkName(name))
                .birthday(birthday)
                .build();
    }

    public String checkName(String name) {
        return (name == null || name.isBlank()) ? login : name;
    }
}
