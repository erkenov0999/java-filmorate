package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genres.interfaces.GenresStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenresService {
    private final GenresStorage genresStorage;

    public List<Genre> findAllGenres() {
        log.info("Получение списка всех жанров");
        return genresStorage.findAllGenres();
    }

    public Optional<Genre> findById(int id) {
        log.info("Получение жанра с ID {}", id);
        return genresStorage.findById(id);
    }
}
