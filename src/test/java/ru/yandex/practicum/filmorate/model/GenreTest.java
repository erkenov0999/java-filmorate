package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    @Test
    void constructor_WithValidParameters_ShouldCreateGenre() {
        // Arrange
        int id = 1;
        String name = "Комедия";

        // Act
        Genre genre = new Genre(id, name);

        // Assert
        assertEquals(id, genre.getId());
        assertEquals(name, genre.getName());
    }

    @Test
    void constructor_WithDifferentParameters_ShouldCreateGenre() {
        // Arrange
        int id = 2;
        String name = "Драма";

        // Act
        Genre genre = new Genre(id, name);

        // Assert
        assertEquals(id, genre.getId());
        assertEquals(name, genre.getName());
    }

    @Test
    void setId_WithValidId_ShouldSetId() {
        // Arrange
        Genre genre = new Genre(1, "Комедия");
        int newId = 3;

        // Act
        genre.setId(newId);

        // Assert
        assertEquals(newId, genre.getId());
    }

    @Test
    void setName_WithValidName_ShouldSetName() {
        // Arrange
        Genre genre = new Genre(1, "Комедия");
        String newName = "Мультфильм";

        // Act
        genre.setName(newName);

        // Assert
        assertEquals(newName, genre.getName());
    }

    @Test
    void equals_WithSameIdAndName_ShouldReturnTrue() {
        // Arrange
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(1, "Комедия");

        // Act & Assert
        assertEquals(genre1, genre2);
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        // Arrange
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(2, "Комедия");

        // Act & Assert
        assertNotEquals(genre1, genre2);
    }

    @Test
    void equals_WithDifferentName_ShouldReturnFalse() {
        // Arrange
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(1, "Драма");

        // Act & Assert
        assertNotEquals(genre1, genre2);
    }
}
