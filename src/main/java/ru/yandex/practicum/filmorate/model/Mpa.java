package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mpa {
    @NotNull(message = "id не может быть пустым")
    private int id;

    @NotNull(message = "Наименование ограничения не может быть пустым")
    private String name;
}
