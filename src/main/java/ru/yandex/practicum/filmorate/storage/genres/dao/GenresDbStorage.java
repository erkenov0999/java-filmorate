package ru.yandex.practicum.filmorate.storage.genres.dao;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.interfaces.GenresStorage;

import java.util.List;
import java.util.Optional;

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
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, genreRowMapper());
    }

    @Override
    public Optional<Genre> findById(int id) {
        try {
            String sql = "SELECT * FROM GENRES WHERE id = ?";
            Genre genre =  jdbcTemplate.queryForObject(sql, genreRowMapper(), id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
           return Optional.empty();
        }
    }
}
