package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    private final FilmService filmService;

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        Film checked = filmService.releaseDateValidation(film);

        checked.setId(filmService.generateId());

        films.put(checked.getId(), checked);
        log.info("Добавлен новый фильм: {}", checked.getName());
        return checked;
    }

    @PutMapping
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

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}