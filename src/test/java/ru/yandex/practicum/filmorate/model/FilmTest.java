package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private Film film;

    @BeforeEach
    void setUp() {
        // Arrange
        film = new Film("Test Film", "Test Description", LocalDate.of(2020, 1, 1), 120);
    }

    @Test
    void constructor_WithValidParameters_ShouldCreateFilm() {
        // Arrange
        String name = "Test Film";
        String description = "Test Description";
        LocalDate releaseDate = LocalDate.of(2020, 1, 1);
        long duration = 120;

        // Act
        Film newFilm = new Film(name, description, releaseDate, duration);

        // Assert
        assertEquals(name, newFilm.getName());
        assertEquals(description, newFilm.getDescription());
        assertEquals(releaseDate, newFilm.getReleaseDate());
        assertEquals(duration, newFilm.getDuration());
        assertNotNull(newFilm.getLikes());
        assertTrue(newFilm.getLikes().isEmpty());
        assertNotNull(newFilm.getGenres());
        assertTrue(newFilm.getGenres().isEmpty());
    }

    @Test
    void setId_WithValidId_ShouldSetId() {
        // Arrange
        Long id = 1L;

        // Act
        film.setId(id);

        // Assert
        assertEquals(id, film.getId());
    }

    @Test
    void setName_WithValidName_ShouldSetName() {
        // Arrange
        String name = "New Film Name";

        // Act
        film.setName(name);

        // Assert
        assertEquals(name, film.getName());
    }

    @Test
    void setDescription_WithValidDescription_ShouldSetDescription() {
        // Arrange
        String description = "New Description";

        // Act
        film.setDescription(description);

        // Assert
        assertEquals(description, film.getDescription());
    }

    @Test
    void setReleaseDate_WithValidDate_ShouldSetReleaseDate() {
        // Arrange
        LocalDate releaseDate = LocalDate.of(2021, 6, 15);

        // Act
        film.setReleaseDate(releaseDate);

        // Assert
        assertEquals(releaseDate, film.getReleaseDate());
    }

    @Test
    void setDuration_WithValidDuration_ShouldSetDuration() {
        // Arrange
        long duration = 150;

        // Act
        film.setDuration(duration);

        // Assert
        assertEquals(duration, film.getDuration());
    }

    @Test
    void setMpa_WithValidMpa_ShouldSetMpa() {
        // Arrange
        Mpa mpa = new Mpa(1, "G");

        // Act
        film.setMpa(mpa);

        // Assert
        assertEquals(mpa, film.getMpa());
    }

    @Test
    void getLikes_ShouldReturnEmptySet() {
        // Arrange
        // (Инициализация в setUp)

        // Act
        Set<Long> likes = film.getLikes();

        // Assert
        assertNotNull(likes);
        assertTrue(likes.isEmpty());
    }

    @Test
    void getLikes_AfterAddingLike_ShouldContainLike() {
        // Arrange
        Long userId = 1L;

        // Act
        film.getLikes().add(userId);

        // Assert
        assertTrue(film.getLikes().contains(userId));
        assertEquals(1, film.getLikes().size());
    }

    @Test
    void getGenres_ShouldReturnEmptySet() {
        // Arrange
        // (Инициализация в setUp)

        // Act
        Set<Genre> genres = film.getGenres();

        // Assert
        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }

    @Test
    void getGenres_AfterAddingGenre_ShouldContainGenre() {
        // Arrange
        Genre genre = new Genre(1, "Комедия");

        // Act
        film.getGenres().add(genre);

        // Assert
        assertTrue(film.getGenres().contains(genre));
        assertEquals(1, film.getGenres().size());
    }
}


