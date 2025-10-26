package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.filmgenres.interfaces.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.mpa.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.storage.genres.interfaces.GenresStorage;
import ru.yandex.practicum.filmorate.storage.filmlikes.interfaces.FilmLikesStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    private FilmController filmController;
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        // Arrange
        FilmStorage filmStorage = null;
        UserStorage userStorage = null;
        FilmGenresStorage filmGenresStorage = null;
        MpaStorage mpaStorage = null;
        GenresStorage genresStorage = null;
        FilmLikesStorage filmLikesStorage = null;
        filmService = new FilmService(filmStorage, userStorage, filmGenresStorage, mpaStorage, genresStorage, filmLikesStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void filmController_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(filmController);
    }

    @Test
    void filmService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(filmService);
    }

    @Test
    void testFilm_ShouldBeCreated() {
        // Arrange
        Film film = new Film("Test Film", "Test Description", LocalDate.of(2020, 1, 1), 120);
        film.setMpa(new Mpa(1, "G"));

        // Act & Assert
        assertNotNull(film);
        assertEquals("Test Film", film.getName());
        assertEquals("Test Description", film.getDescription());
        assertEquals(120, film.getDuration());
        assertNotNull(film.getMpa());
        assertEquals(1, film.getMpa().getId());
        assertEquals("G", film.getMpa().getName());
    }

    @Test
    void testFilm_ShouldHaveEmptyLikes() {
        // Arrange
        Film film = new Film("Test Film", "Test Description", LocalDate.of(2020, 1, 1), 120);

        // Act
        var likes = film.getLikes();

        // Assert
        assertNotNull(likes);
        assertTrue(likes.isEmpty());
    }

    @Test
    void testFilm_ShouldHaveEmptyGenres() {
        // Arrange
        Film film = new Film("Test Film", "Test Description", LocalDate.of(2020, 1, 1), 120);

        // Act
        var genres = film.getGenres();

        // Assert
        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }
}
