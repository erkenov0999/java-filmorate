package ru.yandex.practicum.filmorate.storage.genres.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.interfaces.GenresStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public static RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
              rs.getInt("id"),
                rs.getString("name")
        );
    }



    @Override
    public List<Genre> findAllGenres() {
        log.info("Получение всех жанров");
        String sql = "SELECT * FROM genres";
        List<Genre> genres = jdbcTemplate.query(sql, genreRowMapper());
        log.info("Найдено {} жанров", genres.size());
        return genres;
    }

    @Override
    public Optional<Genre> findById(int id) {
        log.info("Получение жанра с ID: {}", id);
        try {
            String sql = "SELECT * FROM genres WHERE id = ?";
            Genre genre =  jdbcTemplate.queryForObject(sql, genreRowMapper(), id);
            log.info("Жанр с ID {} найден", id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Жанр с ID {} не найден", id);
           return Optional.empty();
        }
    }
}
