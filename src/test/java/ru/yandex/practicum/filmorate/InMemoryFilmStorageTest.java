package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFilmStorageTest {
    private InMemoryFilmStorage filmStorage;
    private Film film;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        film = new Film("Film Name", "Description",
                LocalDate.of(2000, 1, 1), 100);
    }

    @Test
    @DisplayName("Добавление нового фильма")
    void addNewFilm_shouldAddFilmToStorage() {
        // Act
        Film result = filmStorage.addNewFilm(film);

        // Assert
        assertEquals(film, result, "Добавленный фильм должен соответствовать переданному");
        assertEquals(result, filmStorage.getAllFilms().get(0),
                "Фильм должен появиться в хранилище");
    }

    @Test
    @DisplayName("Обновление фильма должно изменять данные в хранилище")
    void updateFilm_shouldModifyExistingFilm() {
        // Arrange
        Film addedFilm = filmStorage.addNewFilm(film);
        addedFilm.setName("Updated Name");

        // Act
        Film updatedFilm = filmStorage.updateFilm(addedFilm);

        // Assert
        assertEquals("Updated Name", updatedFilm.getName(),
                "Имя фильма должно быть обновлено");
        assertEquals(updatedFilm, filmStorage.getFilmById(addedFilm.getId()),
                "Фильм в хранилище должен быть обновлен");
    }

    @Test
    @DisplayName("Попытка обновить несуществующий фильм должна вызывать исключение")
    void updateNonExistentFilm_shouldThrowException() {
        // Arrange
        film.setId(999L);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmStorage.updateFilm(film),
                "Должно быть выброшено исключение при попытке обновить несуществующий фильм");
    }

    @Test
    @DisplayName("Удаление фильма должно удалять его из хранилища")
    void deleteFilm_shouldRemoveFilmFromStorage() {
        // Arrange
        Film addedFilm = filmStorage.addNewFilm(film);
        Long filmId = addedFilm.getId();

        // Act
        filmStorage.deleteFilm(addedFilm);

        // Assert
        assertThrows(ResponseStatusException.class,
                () -> filmStorage.getFilmById(filmId),
                "Фильм должен быть удален из хранилища");
    }

    @Test
    @DisplayName("Попытка удалить несуществующий фильм должна вызывать исключение")
    void deleteNonExistentFilm_shouldThrowException() {
        // Arrange
        film.setId(999L);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmStorage.deleteFilm(film),
                "Должно быть выброшено исключение при попытке удалить несуществующий фильм");
    }

    @Test
    @DisplayName("Получение всех фильмов должно возвращать все добавленные фильмы")
    void getAllFilms_shouldReturnAllAddedFilms() {
        // Arrange
        Film film1 = filmStorage.addNewFilm(film);
        Film film2 = filmStorage.addNewFilm(new Film("Another Film", "Desc",
                LocalDate.of(2001, 1, 1), 120));

        // Act
        List<Film> allFilms = filmStorage.getAllFilms();

        // Assert
        assertEquals(2, allFilms.size(), "Должны быть возвращены все добавленные фильмы");
        assertTrue(allFilms.contains(film1) && allFilms.contains(film2),
                "Возвращенный список должен содержать все добавленные фильмы");
    }

    @Test
    @DisplayName("Получение фильма по ID должно возвращать корректный фильм")
    void getFilmById_shouldReturnCorrectFilm() {
        // Arrange
        Film addedFilm = filmStorage.addNewFilm(film);
        Long filmId = addedFilm.getId();

        // Act
        Film foundFilm = filmStorage.getFilmById(filmId);

        // Assert
        assertEquals(addedFilm, foundFilm, "Найденный фильм должен соответствовать добавленному");
    }

    @Test
    @DisplayName("Попытка получить несуществующий фильм по ID должна вызывать исключение")
    void getNonExistentFilmById_shouldThrowException() {
        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmStorage.getFilmById(999L),
                "Должно быть выброшено исключение при попытке получить несуществующий фильм");
    }

    @Test
    @DisplayName("Генерация ID должна возвращать уникальные значения")
    void generateId_shouldReturnIncrementedValues() {
        // Arrange
        long firstId = filmStorage.generateId();
        long secondId = filmStorage.generateId();

        // Assert
        assertEquals(firstId + 1, secondId, "Каждый новый ID должен быть на 1 больше предыдущего");
    }

    @Test
    @DisplayName("Дата релиза раньше 28 декабря 1895 года должна вызывать исключение")
    void releaseDateValidation_withEarlyDate_shouldThrowException() {
        // Arrange
        Film earlyFilm = new Film("Early Film", "Desc",
                LocalDate.of(1890, 1, 1), 60);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmStorage.addNewFilm(earlyFilm),
                "Должно быть выброшено исключение при попытке добавить фильм с датой релиза раньше 28 декабря 1895 года");
    }

    @Test
    @DisplayName("Фильм с корректной датой релиза должен проходить валидацию")
    void releaseDateValidation_withValidDate_shouldPass() {
        // Arrange
        Film validFilm = new Film("Valid Film", "Desc",
                LocalDate.of(1896, 1, 1), 60);

        // Act
        Film result = filmStorage.addNewFilm(validFilm);

        // Assert
        assertNotNull(result, "Фильм с корректной датой релиза должен быть добавлен");
        assertEquals(validFilm.getName(), result.getName(),
                "Добавленный фильм должен соответствовать переданному");
    }
}