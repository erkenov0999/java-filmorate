package ru.yandex.practicum.filmorate.storage.mpa.dao;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.interfaces.MpaStorage;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private static RowMapper<Mpa> getMpaMapper() {
        return (rs, rowNum) -> new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("SELECT * FROM mpa", getMpaMapper());
    }

    @Override
    public Optional<Mpa> findById(int mpaId) {
        try {
            String sql = "SELECT * FROM mpa WHERE id = ?";
            Mpa mpa = jdbcTemplate.queryForObject(sql, getMpaMapper(), mpaId);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
