package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Set;


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
        log.info("Добавление нового фильма: {}", film);
        filmStorage.addNewFilm(film);
    }

    @PutMapping
    public void updateFilm(@Valid @RequestBody Film film) {
        log.info("Внесение изменений в фильм: {}", film);
        filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public void deleteFilm(@Valid @RequestBody Film film) {
        log.info("Удаление фильма: {}", film);
        filmStorage.deleteFilm(film);
    }

    @GetMapping
    public void getAllFilms() {
        log.info("Получение списка всех фильмов");
        filmStorage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с ID-{}, поставил лайк на фильм с ID-{}", userId, id);
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с ID-{}, убрал свой лайк с фильма с ID-{}", userId, id);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос ТОП фильмов");
       return filmService.getTopFilms(count);
    }
}