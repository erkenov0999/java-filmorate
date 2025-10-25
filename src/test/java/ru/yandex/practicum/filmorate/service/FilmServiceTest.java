package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmServiceTest {

    private FilmService filmService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    private Film testFilm;

    @BeforeEach
    void setUp() {
        // Arrange
        filmStorage = null; // В реальном тесте будет зависимость
        userStorage = null; // В реальном тесте будет зависимость
        filmService = new FilmService(filmStorage, userStorage);

        testFilm = new Film("Test Film", "Test Description", LocalDate.of(2020, 1, 1), 120);
        testFilm.setId(1L);
        testFilm.setMpa(new Mpa(1, "G"));
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
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(testFilm);
        assertEquals("Test Film", testFilm.getName());
        assertEquals("Test Description", testFilm.getDescription());
        assertEquals(120, testFilm.getDuration());
        assertNotNull(testFilm.getMpa());
        assertEquals(1, testFilm.getMpa().getId());
        assertEquals("G", testFilm.getMpa().getName());
    }

    @Test
    void testFilm_ShouldHaveEmptyLikes() {
        // Arrange
        // (Инициализация в setUp)

        // Act
        var likes = testFilm.getLikes();

        // Assert
        assertNotNull(likes);
        assertTrue(likes.isEmpty());
    }

    @Test
    void testFilm_ShouldHaveEmptyGenres() {
        // Arrange
        // (Инициализация в setUp)

        // Act
        var genres = testFilm.getGenres();

        // Assert
        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }
}
