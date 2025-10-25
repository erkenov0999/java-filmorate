package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenresController {
    private final GenresService genresService;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Получение списка всех жанров");
        return genresService.findAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Получение жанра с ID: {}", id);
        return genresService.findById(id).orElse(null);
    }
}
