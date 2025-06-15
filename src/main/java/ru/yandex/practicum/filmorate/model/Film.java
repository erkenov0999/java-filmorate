package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым!")
    private String name;

    @NotBlank(message = "Описание фильма не может быть пустым!")
    @Size(max = 200, message = "Максимальный объем описания фильма должен составлять не более 200 символов!")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой!")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной!")
    private long duration;

    public Film(Long id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film create(String name, String description, LocalDate releaseDate, long duration) {
        return Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDateValidation(releaseDate))
                .duration(duration)
                .build();
    }

    // Метод валидации даты релиза
    private LocalDate releaseDateValidation(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return releaseDate;
    }

}