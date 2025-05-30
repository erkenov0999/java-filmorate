package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Film.
 */
@Data
public class Film {

    long id; //id фильма
    String name; //название фильма
    String description; //описание фильма
    LocalDateTime releaseDate; //дата релиза фильма
    long duration; //продолжительность фильма в минутах
}
