package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void addNewValidFilm() {
        Film film = Film.builder()
                .name("Valid Film")
                .description("Valid description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        Film addedFilm = filmController.addNewFilm(film);
        assertNotNull(addedFilm.getId());
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void addNewFilmWithEarlyReleaseDate() {
        Film film = Film.builder()
                .name("Invalid Film")
                .description("Valid description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(120)
                .build();

        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
    }

    @Test
    void updateFilmWithInvalidId() {
        Film film = Film.builder()
                .id(999L)
                .name("Valid Film")
                .description("Valid description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void getAllFilms() {
        Film film1 = Film.builder()
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();

        Film film2 = Film.builder()
                .name("Film 2")
                .description("Description 2")
                .releaseDate(LocalDate.of(2005, 1, 1))
                .duration(150)
                .build();

        filmController.addNewFilm(film1);
        filmController.addNewFilm(film2);

        assertEquals(2, filmController.getAllFilms().size());
    }
}