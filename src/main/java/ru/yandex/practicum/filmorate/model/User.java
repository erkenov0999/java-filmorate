package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Set<Long> friends = new HashSet<>();

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

    // Конструктор для создания нового пользователя (без ID)
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    // Конструктор для загрузки из БД (с ID)
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}