package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма: {}", film);
        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Внесение изменений в фильм: {}", film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping
    public void deleteFilm(@Valid @RequestBody Film film) {
        log.info("Удаление фильма: {}", film);
        filmService.deleteFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Получение фильма с ID: {}", id);
        return filmService.getFilmById(id)
                .orElse(null);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с ID-{}, поставил лайк на фильм с ID-{}", userId, id);
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с ID-{}, убрал свой лайк с фильма с ID-{}", userId, id);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос ТОП фильмов");
        return filmService.getTopFilms(count);
    }
}