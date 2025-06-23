package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmService {
    public static final LocalDate RELEASE_DATE_LOWER_BOUND = LocalDate.of(1895, 12, 28);
    private long idCounter = 1;

    public Film releaseDateValidation(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE_LOWER_BOUND)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return film;
    }

    public Long generateId() {
        return idCounter++;
    }
}
