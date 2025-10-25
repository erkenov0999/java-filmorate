package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GenresControllerTest {

    private GenresController genresController;
    private GenresService genresService;

    @BeforeEach
    void setUp() {
        // Arrange
        genresService = new GenresService(null); // В реальном тесте будет зависимость
        genresController = new GenresController(genresService);
    }

    @Test
    void genresController_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(genresController);
    }

    @Test
    void genresService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(genresService);
    }

    @Test
    void testGenre_ShouldBeCreated() {
        // Arrange
        Genre genre = new Genre(1, "Комедия");

        // Act & Assert
        assertNotNull(genre);
        assertEquals(1, genre.getId());
        assertEquals("Комедия", genre.getName());
    }
}
