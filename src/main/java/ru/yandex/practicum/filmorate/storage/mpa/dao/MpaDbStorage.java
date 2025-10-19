package ru.yandex.practicum.filmorate.storage.mpa.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.interfaces.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Получение всех MPA рейтингов");
        List<Mpa> mpaList = jdbcTemplate.query("SELECT * FROM mpa", getMpaMapper());
        log.info("Найдено {} MPA рейтингов", mpaList.size());
        return mpaList;
    }

    @Override
    public Optional<Mpa> findById(int mpaId) {
        log.info("Получение MPA рейтинга с ID: {}", mpaId);
        try {
            String sql = "SELECT * FROM mpa WHERE id = ?";
            Mpa mpa = jdbcTemplate.queryForObject(sql, getMpaMapper(), mpaId);
            log.info("MPA рейтинг с ID {} найден", mpaId);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException e) {
            log.warn("MPA рейтинг с ID {} не найден", mpaId);
            return Optional.empty();
        }
    }
}
