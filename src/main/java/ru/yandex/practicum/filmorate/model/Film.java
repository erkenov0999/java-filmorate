package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
@Builder
public class Film {

    private static long idCounter = 1; //статический счетчик для генерации id
    private long id; //id фильма

    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name; //название фильма

    @NotBlank(message = "Описание фильма не может быть пустым!")
    @Size(message = "Максимальный объем описания фильма должен составлять не более 200 символов!")
    private String description; //описание фильма

    @NotBlank(message = "Дата релиза не может быть пустой!")
    private LocalDate releaseDate; //дата релиза фильма

    @NotBlank(message = "Продолжительность фильма не может быть пустой!")
    @Positive(message = "Продолжительность фильма не может быть меньше 0 минут!")
    private long duration; //продолжительность фильма в минутах

    public Film createFilm(String name, String description, LocalDate releaseDate, long duration) { //конструктор
        return Film.builder()
                .id(idCounter++)
                .name(name)
                .description(description)
                .releaseDate(checkReleaseDate(releaseDate))
                .duration(duration)
                .build();
    }

    public LocalDate checkReleaseDate(LocalDate releaseDate) throws ValidationException { //метод валидации
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return releaseDate;
    }
}
