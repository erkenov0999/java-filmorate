package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmgenres.interfaces.FilmGenresStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmGenresService {
    private final FilmGenresStorage filmGenresStorage;

    public void addFilmGenre(Long filmId, Integer genreId) {
        log.info("Добавление жанра с ID {} к фильму с ID {}", genreId, filmId);
        filmGenresStorage.addFilmGenre(filmId, genreId);
    }

    public void deleteFilmGenresByFilmId(Long filmId) {
        log.info("Удаление всех жанров фильма с ID {}", filmId);
        filmGenresStorage.deleteFilmGenresByFilmId(filmId);
    }

    public List<Genre> getGenresByFilmId(Long filmId) {
        log.info("Получение жанров фильма с ID {}", filmId);
        return filmGenresStorage.getGenresByFilmId(filmId);
    }
}

