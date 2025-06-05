package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;

    @NotBlank(message = "Email не может быть пустым!")
    @Email(message = "Введите корректный email!")
    private String email;

    @NotBlank(message = "Логин не может быть пустым!")
    @Pattern(regexp = "^\\S+$", message = "Пробелы в логине запрещены!")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой!")
    @PastOrPresent(message = "Дата рождения не может быть позже текущего дня!")
    private LocalDate birthday;

    public User create(String email, String login, String name, LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(nameValidation(name))
                .birthday(birthday)
                .build();
    }

    // Метод валидации имени
    public String nameValidation(String name) {
        if (!(name == null || name.isBlank())) {
            return name;
        }
        return login;
    }
}