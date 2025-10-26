package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.filmgenres.dao.FilmGenresDbStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmGenresServiceTest {

    private FilmGenresService filmGenresService;
    private FilmGenresDbStorage filmGenresDbStorage;

    @BeforeEach
    void setUp() {
        // Arrange
        filmGenresDbStorage = new FilmGenresDbStorage(null); // В реальном тесте будет JdbcTemplate
        filmGenresService = new FilmGenresService(filmGenresDbStorage);
    }

    @Test
    void filmGenresService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(filmGenresService);
    }

    @Test
    void filmGenresDbStorage_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(filmGenresDbStorage);
    }
}