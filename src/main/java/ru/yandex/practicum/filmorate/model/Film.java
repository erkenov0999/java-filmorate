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

    public static FilmBuilder builder() {
        return new CustomFilmBuilder();
    }

    private static class CustomFilmBuilder extends FilmBuilder {
        @Override
        public Film build() {
            Film film = super.build();
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            return film;
        }
    }
}