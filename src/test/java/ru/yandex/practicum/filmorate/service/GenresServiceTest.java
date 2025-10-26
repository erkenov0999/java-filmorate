package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.genres.dao.GenresDbStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenresServiceTest {

    private GenresService genresService;
    private GenresDbStorage genresDbStorage;

    @BeforeEach
    void setUp() {
        // Arrange
        genresDbStorage = new GenresDbStorage(null); // В реальном тесте будет JdbcTemplate
        genresService = new GenresService(genresDbStorage);
    }

    @Test
    void genresService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(genresService);
    }

    @Test
    void genresDbStorage_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(genresDbStorage);
    }
}