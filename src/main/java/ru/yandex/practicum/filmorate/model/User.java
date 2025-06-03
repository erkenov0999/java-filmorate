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

    public static UserBuilder builder() {
        return new CustomUserBuilder();
    }

    private static class CustomUserBuilder extends UserBuilder {
        @Override
        public User build() {
            User user = super.build();
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return user;
        }
    }
}