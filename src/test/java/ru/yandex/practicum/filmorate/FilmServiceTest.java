package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private FilmService filmService;
    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
    }

    @Test
    @DisplayName("Добавление лайка должно увеличить количество лайков фильма")
    void putLike_shouldAddLikeToFilm() {
        // Arrange
        Film film = new Film("Film Name", "Description",
                LocalDate.of(2000, 1, 1), 100);
        User user = new User("user@mail.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        filmStorage.addNewFilm(film);
        userStorage.addNewUser(user);

        // Act
        filmService.putLike(film.getId(), user.getId());

        // Assert
        assertTrue(film.getLikes().contains(user.getId()),
                "Лайк пользователя должен быть добавлен в список лайков фильма");
    }

    @Test
    @DisplayName("Попытка добавить лайк несуществующему фильму должна вызывать исключение")
    void putLike_withNonExistentFilm_shouldThrowException() {
        // Arrange
        User user = new User("user@mail.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        userStorage.addNewUser(user);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmService.putLike(999L, user.getId()),
                "Должно быть выброшено исключение при попытке поставить лайк несуществующему фильму");
    }

    @Test
    @DisplayName("Удаление лайка должно уменьшить количество лайков фильма")
    void removeLike_shouldRemoveLikeFromFilm() {
        // Arrange
        Film film = new Film("Film Name", "Description",
                LocalDate.of(2000, 1, 1), 100);
        User user = new User("user@mail.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        filmStorage.addNewFilm(film);
        userStorage.addNewUser(user);
        filmService.putLike(film.getId(), user.getId());

        // Act
        filmService.removeLike(film.getId(), user.getId());

        // Assert
        assertFalse(film.getLikes().contains(user.getId()),
                "Лайк пользователя должен быть удален из списка лайков фильма");
    }

    @Test
    @DisplayName("Попытка удалить несуществующий лайк должна вызывать исключение")
    void removeLike_withNonExistentLike_shouldThrowException() {
        // Arrange
        Film film = new Film("Film Name", "Description",
                LocalDate.of(2000, 1, 1), 100);
        User user = new User("user@mail.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        filmStorage.addNewFilm(film);
        userStorage.addNewUser(user);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmService.removeLike(film.getId(), user.getId()),
                "Должно быть выброшено исключение при попытке удалить несуществующий лайк");
    }

    @Test
    @DisplayName("Попытка добавить повторный лайк должна вызывать исключение")
    void putLike_withDuplicateLike_shouldThrowException() {
        // Arrange
        Film film = new Film("Film Name", "Description",
                LocalDate.of(2000, 1, 1), 100);
        User user = new User("user@mail.ru", "user", "User",
                LocalDate.of(1990, 1, 1));
        filmStorage.addNewFilm(film);
        userStorage.addNewUser(user);
        filmService.putLike(film.getId(), user.getId());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> filmService.putLike(film.getId(), user.getId()),
                "Должно быть выброшено исключение при попытке поставить повторный лайк");
    }

    @Test
    @DisplayName("Получение топа фильмов должно возвращать фильмы в порядке убывания лайков")
    void getTopFilms_shouldReturnFilmsOrderedByLikes() {
        // Arrange
        Film film1 = new Film("Film 1", "Description 1",
                LocalDate.of(2000, 1, 1), 100);
        Film film2 = new Film("Film 2", "Description 2",
                LocalDate.of(2001, 1, 1), 100);
        User user1 = new User("user1@mail.ru", "user1", "User1",
                LocalDate.of(1990, 1, 1));
        User user2 = new User("user2@mail.ru", "user2", "User2",
                LocalDate.of(1991, 1, 1));

        filmStorage.addNewFilm(film1);
        filmStorage.addNewFilm(film2);
        userStorage.addNewUser(user1);
        userStorage.addNewUser(user2);

        filmService.putLike(film1.getId(), user1.getId());
        filmService.putLike(film1.getId(), user2.getId());
        filmService.putLike(film2.getId(), user1.getId());

        // Act
        List<Film> topFilms = filmService.getTopFilms(2);

        // Assert
        assertEquals(2, topFilms.size(), "Должно вернуться 2 фильма");
        assertEquals(film1.getId(), topFilms.get(0).getId(),
                "Первый фильм должен быть с наибольшим количеством лайков");
        assertEquals(film2.getId(), topFilms.get(1).getId(),
                "Второй фильм должен быть с меньшим количеством лайков");
    }
}