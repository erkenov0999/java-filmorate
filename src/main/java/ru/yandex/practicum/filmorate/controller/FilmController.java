package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    InMemoryFilmStorage filmStorage;

    @PostMapping
    public void addNewFilm(@Valid @RequestBody Film film) {
        filmStorage.addNewFilm(film);
    }

    @PutMapping
    public void updateFilm(@Valid @RequestBody Film film) {
        filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public void deleteFilm(@Valid @RequestBody Film film) {
        filmStorage.deleteFilm(film);
    }

    @GetMapping
    public void getAllFilms() {
        filmStorage.getAllFilms();
    }
}