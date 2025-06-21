package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        FilmService filmService = new FilmService();
        filmController = new FilmController(filmService);

        testFilm = new Film(
                "Test Movie",
                "Test Description",
                LocalDate.of(2000, 1, 1),
                120
        );
    }

    @Test
    @DisplayName("При добавлении фильма возвращается корректный фильм")
    void addNewFilm_returns_correct_film() {
        Film result = filmController.addNewFilm(testFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Movie", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), result.getReleaseDate());
        assertEquals(120, result.getDuration());
    }

    @Test
    @DisplayName("При обновлении фильма данные изменяются корректно")
    void updateFilm_changes_film_data_correctly() {
        Film addedFilm = filmController.addNewFilm(testFilm);

        Film updatedFilm = new Film(
                "Updated Movie",
                "Updated Description",
                LocalDate.of(2001, 1, 1),
                150
        );
        updatedFilm.setId(addedFilm.getId());

        Film result = filmController.updateFilm(updatedFilm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Movie", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(LocalDate.of(2001, 1, 1), result.getReleaseDate());
        assertEquals(150, result.getDuration());
    }

    @Test
    @DisplayName("При обновлении несуществующего фильма выбрасывается исключение")
    void updateFilm_throws_exception_when_film_not_found() {
        testFilm.setId(999L);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.updateFilm(testFilm));

        assertEquals("Фильм с ID 999 не найден", exception.getMessage());
    }

    @Test
    @DisplayName("При отсутствии фильмов возвращается пустой список")
    void getAllFilms_returns_empty_list_when_no_films() {
        List<Film> result = filmController.getAllFilms();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("При наличии фильмов возвращается их список")
    void getAllFilms_returns_all_films_when_films_exist() {
        filmController.addNewFilm(testFilm);

        List<Film> result = filmController.getAllFilms();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getName());
        assertEquals("Test Description", result.get(0).getDescription());
    }

    @Test
    @DisplayName("При добавлении фильма генерируется новый ID")
    void addNewFilm_generates_new_id_for_each_film() {
        Film firstFilm = filmController.addNewFilm(testFilm);
        Film secondFilm = filmController.addNewFilm(new Film(
                "Second Movie",
                "Second Description",
                LocalDate.of(2002, 1, 1),
                90
        ));

        assertEquals(1L, firstFilm.getId());
        assertEquals(2L, secondFilm.getId());
    }

    @Test
    @DisplayName("При дате релиза раньше 28 декабря 1895 года выбрасывается исключение")
    void addNewFilm_throws_exception_when_release_date_too_early() {
        Film oldFilm = new Film(
                "Old Movie",
                "Old Description",
                LocalDate.of(1895, 12, 27),
                60
        );

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.addNewFilm(oldFilm));

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("При обновлении с датой релиза раньше 28 декабря 1895 года выбрасывается исключение")
    void updateFilm_throws_exception_when_release_date_too_early() {
        Film addedFilm = filmController.addNewFilm(testFilm);

        Film invalidFilm = new Film(
                "Invalid Movie",
                "Invalid Description",
                LocalDate.of(1895, 12, 27),
                60
        );
        invalidFilm.setId(addedFilm.getId());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.updateFilm(invalidFilm));

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }
}