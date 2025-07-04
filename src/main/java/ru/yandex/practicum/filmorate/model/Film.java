package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private Set<Long> likes;

    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name;

    @NotBlank(message = "Описание фильма не может быть пустым!")
    @Size(max = 200, message = "Максимальный объем описания фильма должен составлять не более 200 символов!")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой!")

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной!")
    private long duration;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}