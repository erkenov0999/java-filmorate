package ru.yandex.practicum.filmorate.storage.filmgenres.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmgenres.interfaces.FilmGenresStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.genres.dao.GenresDbStorage.genreRowMapper;

@AllArgsConstructor
@Repository
public class FilmGenresDbStorage implements FilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(Long filmId, Integer genreId) {
        String sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    @Override
    public void deleteFilmGenresByFilmId(Long filmId) {
        String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        String sql = "SELECT genres.id, genres.name " +
                     "FROM film_genres " +
                     "JOIN genres ON film_genres.genre_id = genres.id " +
                     "WHERE genres.film_id = ?";

        return jdbcTemplate.query(sql, genreRowMapper(), filmId);

    }
}
