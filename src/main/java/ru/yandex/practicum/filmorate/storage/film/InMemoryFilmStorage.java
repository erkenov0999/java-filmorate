package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    private final FilmService filmService;

    @Autowired
    public InMemoryFilmStorage(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    public Film addNewFilm(@Valid @RequestBody Film film) {
        Film checked = filmService.releaseDateValidation(film);

        checked.setId(filmService.generateId());

        films.put(checked.getId(), checked);
        log.info("Добавлен новый фильм: {}", checked.getName());
        return checked;
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film) throws ResponseStatusException{
        Long id = film.getId();

        if (id == null || !films.containsKey(id)) {
            log.error("При обновлении фильма с ID {} , произошла ошибка, фильм не найден", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
        }

        Film checked = filmService.releaseDateValidation(film);

        films.put(checked.getId(), checked);
        log.info("Обновлен фильм: {}", checked.getName());
        return checked;
    }

    @Override
    public void deleteFilm(Film film) throws ResponseStatusException {
        Long id = film.getId();

        if (id == null || !films.containsKey(id)) {
            log.error("При удалении фильма с ID {} произошла ошибка, фильм не найден", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
        }

        films.remove(id);
        log.info("Удален фильм: {}", film.getName());
        System.out.println("Фильм \"" + film.getName() + "\" удален");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) throws ResponseStatusException {
        if (!films.containsKey(id)) {
            log.error("Фильм не найден по ID {}, так как отсутсвует в хранилище", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден!");
        }

        log.info("Поиск фильма по ID: {}", films.get(id).getName());
        return films.get(id);
    }
}
