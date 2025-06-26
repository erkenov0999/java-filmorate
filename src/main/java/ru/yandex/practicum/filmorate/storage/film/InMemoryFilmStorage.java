package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    FilmService filmService;


    @Override
    public Film addNewFilm(@Valid @RequestBody Film film) {
        Film checked = filmService.releaseDateValidation(film);

        checked.setId(filmService.generateId());

        films.put(checked.getId(), checked);
        log.info("Добавлен новый фильм: {}", checked.getName());
        return checked;
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film) {
        Long id = film.getId();

        if (id == null || !films.containsKey(id)) {
            throw new ValidationException("Фильм с ID " + id + " не найден");
        }

        Film checked = filmService.releaseDateValidation(film);

        films.put(checked.getId(), checked);
        log.info("Обновлен фильм: {}", checked.getName());
        return checked;
    }

    @Override
    public void deleteFilm(Film film) {
        Long id = film.getId();

        if (id == null || !films.containsKey(id)) {
            throw new ValidationException("Фильм \"" + film.getName() + "\" не найден");
        }

        films.remove(id);
        log.info("Удален фильм: {}", film.getName());
        System.out.println("Фильм \"" + film.getName() + "\" удален");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
