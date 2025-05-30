package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@RestController
public class FilmController {

    private Map<Long, Film> films;

    @PostMapping("/add-new-film")
    public Film addNewFilms (Film film) {
        films.put(film.getId(), film);
        System.out.println("Фильм успешно добавлен!");
        return film;
    }

    @PutMapping("/update-film")
    public Film updateFilm(Film film) {
        try {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
                System.out.println("Фильм успешно обновлен!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Фильма с таким id не найдено" + e.getMessage());
        }
        return film;
    }

    @GetMapping("/all-films")
    public Map<Long, Film> getAllFilms() {
        return films;
    }

}
