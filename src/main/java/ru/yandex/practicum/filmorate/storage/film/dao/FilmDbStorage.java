package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.interfaces.FilmStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private static RowMapper<Film> getFilmMapper() {
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
            
            return film;
        };
    }

    @Override
    public Film addNewFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
                film.getName(), 
                film.getDescription(), 
                film.getReleaseDate(), 
                film.getDuration(),
                film.getMpa().getId());
        
        // Получаем ID созданного фильма
        String getIdSql = "SELECT id FROM films WHERE name = ? AND description = ? ORDER BY id DESC LIMIT 1";
        Long filmId = jdbcTemplate.queryForObject(getIdSql, Long.class, film.getName(), film.getDescription());
        film.setId(filmId);
        
        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
                film.getName(), 
                film.getDescription(), 
                film.getReleaseDate(), 
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        
        return getFilmById(film.getId());
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
        String sql = "SELECT f.*, m.name as mpa_name FROM films f LEFT JOIN mpa m ON f.mpa_id = m.id";
        return jdbcTemplate.query(sql, getFilmMapper());
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT f.*, m.name as mpa_name FROM films f LEFT JOIN mpa m ON f.mpa_id = m.id WHERE f.id = ?";
        List<Film> films = jdbcTemplate.query(sql, getFilmMapper(), id);
        return films.isEmpty() ? null : films.get(0);
    }
}
