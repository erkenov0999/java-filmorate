package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmgenres.interfaces.FilmGenresStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresStorage filmGenresStorage;

    private RowMapper<Film> getFilmMapper() {
        return (rs, rowNum) -> {
            Film film = new Film(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getLong("duration")
            );
            film.setId(rs.getLong("id"));
            // Создаем объект MPA из данных в ResultSet
            int mpaId = rs.getInt("mpa_id");
            String mpaName = rs.getString("mpa_name");
            Mpa mpa = new Mpa(mpaId, mpaName);
            film.setMpa(mpa);
            
            // Загружаем жанры для фильма
            film.getGenres().addAll(filmGenresStorage.getGenresByFilmId(film.getId()));
            
            return film;
        };
    }

    private static Map<String, Object> filmToMap(Film film) {
        return Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "mpa_id", film.getMpa() != null ? film.getMpa().getId() : null
        );
    }

    @Override
    public Film addNewFilm(Film film) {
        log.info("Добавление нового фильма: {}", film.getName());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Long filmId = simpleJdbcInsert.executeAndReturnKey(filmToMap(film))
                .longValue();
        film.setId(filmId);
        log.info("Фильм успешно добавлен с ID: {}", filmId);
        return getFilmById(filmId)
                .orElse(null);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма с ID: {}", film.getId());
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());
        if (rowsAffected == 0) {
            log.warn("Фильм с ID {} не найден для обновления", film.getId());
            return null;
        }
        log.info("Фильм с ID {} успешно обновлен", film.getId());
        return getFilmById(film.getId())
                .orElse(null);
    }

    @Override
    public void deleteFilm(Film film) {
        deleteFilmById(film.getId());
    }

    private void deleteFilmById(long id) {
        String sql = "DELETE FROM films WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            log.warn("Фильм с id {} не найден для удаления", id);
        } else {
            log.info("Фильм с id {} успешно удален", id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        String sql = "SELECT f.*, m.name as mpa_name FROM films f LEFT JOIN mpa m ON f.mpa_id = m.id";
        List<Film> films = jdbcTemplate.query(sql, getFilmMapper());
        log.info("Найдено {} фильмов", films.size());
        return films;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        log.info("Получение фильма с ID: {}", id);
        String sql = "SELECT f.*, m.name as mpa_name FROM films f LEFT JOIN mpa m ON f.mpa_id = m.id WHERE f.id = ?";
        try {
            List<Film> films = jdbcTemplate.query(sql, getFilmMapper(), id);
            if (films.isEmpty()) {
                log.warn("Фильм с ID {} не найден", id);
                return Optional.empty();
            }
            log.info("Фильм с ID {} найден", id);
            return Optional.of(films.get(0));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Фильм с ID {} не найден", id);
            return Optional.empty();
        }
    }
}
