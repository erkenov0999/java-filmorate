package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre implements Comparable<Genre> {
    @NotNull(message = "id не может быть пустым")
    private int id;

    @NotNull(message = "Наименование жанра не может быть пустым")
    private String name;

    @Override
    public int compareTo(Genre other) {
        return Integer.compare(this.id, other.id);
    }
}
