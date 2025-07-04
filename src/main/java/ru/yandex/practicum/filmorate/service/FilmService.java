package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final Set<Film> topFilms = new TreeSet<>(
            Comparator.comparingInt((Film film) -> -film.getLikes().size())
                    .thenComparing(Film::getId));

    private final InMemoryFilmStorage filmStorage;


    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    public void putLike(long idFilm, long idUser) throws ResponseStatusException {
        Film film = filmStorage.getFilmById(idFilm);

        if(film.getLikes().contains(idUser)) {
            log.error("Попытка повторно поставить лайк фильму");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пользователь с ID " + idUser + " уже поставил лайк данному фильму.");
        }

        film.getLikes().add(idUser);
        updateTopFilms(film);
        log.info("Пользователь с идентификатором {} поставил лайк на фильм с id {}.", idUser, film.getId());
    }

    public void removeLike(long idFilm, long idUser) throws ResponseStatusException {
        Film film = filmStorage.getFilmById(idFilm);

        if (!film.getLikes().contains(idUser)) {
            log.error("Попытка убрать не существующий лайк");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Убрать лайк нельзя, так как пользователь c id " + idUser
                            + " не ставил лайк на фильм с id " + film.getId());
        }

        film.getLikes().remove(idUser);
        updateTopFilms(film);
        log.info("Пользователь {} убрал лайк с фильма с id {}", idUser, film.getId());
    }

    private void updateTopFilms(Film film) {
        topFilms.remove(film);
        topFilms.add(film);

        if (topFilms.size() > 10) {
            Film lastFilm = topFilms.stream()
                    .skip(10 - 1)
                    .findFirst()
                    .orElse(null);
            topFilms.remove(lastFilm);
        }
    }

    public Set<Film> getTopFilms(int limit) {
        if (limit < 0) {
            log.error("Запрашиваемый топ меньше 0");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Запрашиваемый топ не может быть отрицательным!");
        }

        if (limit == 0) {
            limit = 10;
        }

        log.info("Запрошен топ-{} фильмов, ТОП-", limit);
        return topFilms.stream()
                .limit(limit)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
