package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private List<Film> topFilms = new ArrayList<>();

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;


    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public void putLike(long idFilm, long idUser) throws ResponseStatusException {
        chekingFilmAndUser(idFilm, idUser);

        Film film = filmStorage.getFilmById(idFilm);

        if(film.getLikes().contains(idUser)) {
            log.error("Попытка повторно поставить лайк фильму");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пользователь с ID " + idUser + " уже поставил лайк данному фильму.");
        }

        film.getLikes().add(idUser);
        updateAndSortFilms(film);
        log.info("Пользователь с идентификатором {} поставил лайк на фильм с id {}.", idUser, film.getId());
    }

    public void removeLike(long idFilm, long idUser) throws ResponseStatusException {
        chekingFilmAndUser(idFilm, idUser);

        Film film = filmStorage.getFilmById(idFilm);

        if (!film.getLikes().contains(idUser)) {
            log.error("Попытка убрать не существующий лайк");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Убрать лайк нельзя, так как пользователь c id " + idUser
                            + " не ставил лайк на фильм с id " + film.getId());
        }

        film.getLikes().remove(idUser);
        updateAndSortFilms(film);
        log.info("Пользователь {} убрал лайк с фильма с id {}", idUser, film.getId());
    }

    private void updateAndSortFilms(Film film) throws ResponseStatusException {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        if (films.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Список фильмов пока еще пуст");
        }

        films.sort(Comparator.comparingInt((Film movie) -> film.getLikes().size()).reversed());
        topFilms = films;
        log.info("Произошли обновления в топе фильмов");
    }

    public List<Film> getTopFilms(int limit) {
        List<Film> films = new ArrayList<>(topFilms);
        List<Film> topFilms = new ArrayList<>();
        long listSize = films.size();

        if (limit <= 0) {
            for (int i = 0; i < 11 && i < listSize; i++) {
                topFilms.add(films.get(i));
            }
        }

        for (int i = 0; i < limit && i < listSize; i++) {
            topFilms.add(films.get(i));
        }
        return topFilms;
    }

    private void chekingFilmAndUser(long filmId, long userId) throws ResponseStatusException {
        if (userStorage.getUserById(userId) == null) {
            log.error("Не удалось найти пользователя с ID {} ", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + userId);
        }

        if (filmStorage.getFilmById(filmId) == null) {
            log.error("Не удалось найти фильм с ID {} ", filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти фильм с ID " + filmId);
        }
    }
}
