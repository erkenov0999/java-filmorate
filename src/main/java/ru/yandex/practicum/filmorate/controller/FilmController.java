package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film addNewFilms(@RequestBody Film film) {
        films.put(film.getId(), film);
        System.out.println("Фильм успешно добавлен!");

        log.info("Добавлен новый фильм {}", film.getName());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        try {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
                System.out.println("Фильм успешно обновлен!");

                log.info("Обновление фильма {} прошло успешно", film.getName());
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());

            throw new ValidationException("Фильма с таким id не найдено" + e.getMessage());
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<Film>(films.values());
    }
}
