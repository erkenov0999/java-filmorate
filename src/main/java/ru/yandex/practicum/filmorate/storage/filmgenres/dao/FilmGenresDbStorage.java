package ru.yandex.practicum.filmorate.storage.filmgenres.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmgenres.interfaces.FilmGenresStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.genres.dao.GenresDbStorage.genreRowMapper;

@Slf4j
@AllArgsConstructor
@Repository
public class FilmGenresDbStorage implements FilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(Long filmId, Integer genreId) {
        log.info("Добавление жанра {} к фильму {}", genreId, filmId);
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, genreId);
        log.info("Жанр {} успешно добавлен к фильму {}", genreId, filmId);
    }

    @Override
    public void deleteFilmGenresByFilmId(Long filmId) {
        log.info("Удаление всех жанров для фильма {}", filmId);
        String sql = "DELETE FROM film_genres WHERE film_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, filmId);
        log.info("Удалено {} жанров для фильма {}", rowsAffected, filmId);
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        log.info("Получение жанров для фильма {}", filmId);
        String sql = "SELECT genres.id, genres.name " +
                     "FROM film_genres " +
                     "JOIN genres ON film_genres.genre_id = genres.id " +
                     "WHERE film_genres.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, genreRowMapper(), filmId);
        log.info("Найдено {} жанров для фильма {}", genres.size(), filmId);
        return genres;
    }
}
