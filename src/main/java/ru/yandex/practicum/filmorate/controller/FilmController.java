package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        // Установка ID
        film.setId(idCounter++);

        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с ID " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film.getName());
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}