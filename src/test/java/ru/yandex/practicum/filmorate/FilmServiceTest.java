package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
//    //private final FilmService filmService = new FilmService();
//
//    @Test
//    @DisplayName("Если дата релиза раньше 28 декабря 1895 года, валидация не пройдет")
//    void releaseDate_is_before_minimum_date() {
//        // Arrange
//        Film film = new Film("Test movie", "Testing movie",
//                LocalDate.of(1895, 12, 27), 100);
//
//        // Act & Assert
//        ValidationException exception = assertThrows(ValidationException.class,
//                () -> filmService.releaseDateValidation(film));
//
//        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("Если дата релиза равна 28 декабря 1895 года, валидация пройдет")
//    void releaseDate_is_equal_to_minimum_date() {
//        //Arrange
//        Film film = new Film("Test movie", "Testing movie",
//                LocalDate.of(1895, 12, 28), 100);
//
//        //Act
//        Film validatedFilm = filmService.releaseDateValidation(film);
//
//        //Assert
//        assertEquals(film, validatedFilm, "Фильм с минимально допустимой датой должен пройти валидацию");
//    }
//
//    @Test
//    @DisplayName("Если дата релиза позже 28 декабря 1895 года, валидация пройдет")
//    void releaseDate_is_after_minimum_date() {
//        //Arrange
//        Film film = new Film("Test movie", "Testing movie",
//                LocalDate.of(1895, 12, 29), 100);
//
//        //Act
//        Film validatedFilm = filmService.releaseDateValidation(film);
//
//        //Assert
//        assertEquals(film, validatedFilm, "Фильм с датой релиза после минимальной должен пройти валидацию");
//    }
//
//    @Test
//    @DisplayName("Генератор ID должен возвращать последовательные значения")
//    void generateId_returns_incremented_values() {
//        //Act
//        long firstId = filmService.generateId();
//        long secondId = filmService.generateId();
//
//        //Assert
//        assertEquals(1, firstId, "Первый ID должен быть 1");
//        assertEquals(2, secondId, "Второй ID должен быть 2");
//    }
}
