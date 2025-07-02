package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private InMemoryFilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

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

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public void getPopularFilms(@PathVariable int count) {
        filmService.getTopFilms(count);
    }
}