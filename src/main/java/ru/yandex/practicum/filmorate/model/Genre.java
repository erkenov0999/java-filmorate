package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Genre {
    @NotNull(message = "id не может быть пустым")
    private int id;

    @NotNull(message = "Наименование жанра не может быть пустым")
    private String genreName;
}
