package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.filmgenres.interfaces.FilmGenresStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenresStorage filmGenresStorage;
    private List<Film> topFilms = new ArrayList<>();

    public Film addNewFilm(Film film) {
        log.info("Добавление нового фильма: {}", film.getName());
        Film savedFilm = filmStorage.addNewFilm(film);
        // Сохраняем жанры фильма
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenresStorage.deleteFilmGenresByFilmId(savedFilm.getId());
            film.getGenres().forEach(genre ->
                    filmGenresStorage.addFilmGenre(savedFilm.getId(), genre.getId())
            );
        }
        return savedFilm;
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма с ID: {}", film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        // Обновляем жанры фильма
        if (film.getGenres() != null) {
            filmGenresStorage.deleteFilmGenresByFilmId(film.getId());
            film.getGenres().forEach(genre ->
                    filmGenresStorage.addFilmGenre(film.getId(), genre.getId())
            );
        }
        return updatedFilm;
    }

    public void deleteFilm(Film film) {
        log.info("Удаление фильма с ID: {}", film.getId());
        filmStorage.deleteFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Optional<Film> getFilmById(long id) {
        log.info("Получение фильма с ID: {}", id);
        return filmStorage.getFilmById(id);
    }

    public void putLike(long idFilm, long idUser) throws ResponseStatusException {
        checkingFilmAndUser(idFilm, idUser);
        Optional<Film> filmOpt = filmStorage.getFilmById(idFilm);
        if (filmOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        Film film = filmOpt.get();
        if (film.getLikes().contains(idUser)) {
            log.error("Попытка повторно поставить лайк фильму");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пользователь с ID " + idUser + " уже поставил лайк данному фильму.");
        }
        film.getLikes().add(idUser);
        updateAndSortFilms();
        log.info("Пользователь с идентификатором {} поставил лайк на фильм с id {}.", idUser, film.getId());
    }

    public void removeLike(long idFilm, long idUser) throws ResponseStatusException {
        checkingFilmAndUser(idFilm, idUser);

        Optional<Film> filmOpt = filmStorage.getFilmById(idFilm);
        if (filmOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        Film film = filmOpt.get();
        if (!film.getLikes().contains(idUser)) {
            log.error("Попытка убрать не существующий лайк");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Убрать лайк нельзя, так как пользователь c id " + idUser
                            + " не ставил лайк на фильм с id " + film.getId());
        }
        film.getLikes().remove(idUser);
        updateAndSortFilms();
        log.info("Пользователь {} убрал лайк с фильма с id {}", idUser, film.getId());
    }

    private void updateAndSortFilms() throws ResponseStatusException {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        if (films.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Список фильмов пока еще пуст");
        }

        films.sort(Comparator.comparingInt((Film movie) -> movie.getLikes().size()).reversed());
        topFilms = films;
        log.info("Произошли обновления в топе фильмов");
    }

    public List<Film> getTopFilms(int limit) {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        if (films.isEmpty()) {
            return films;
        }

        films.sort(Comparator.comparingInt((Film movie) -> movie.getLikes().size()).reversed());
        List<Film> resultFilms = new ArrayList<>();
        long listSize = films.size();
        for (int i = 0; i < limit && i < listSize; i++) {
            resultFilms.add(films.get(i));
        }
        return resultFilms;
    }

    private void checkingFilmAndUser(long filmId, long userId) throws ResponseStatusException {
        if (userStorage.findUserById(userId).isEmpty()) {
            log.error("Не удалось найти пользователя с ID {} ", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID " + userId);
        }

        if (filmStorage.getFilmById(filmId).isEmpty()) {
            log.error("Не удалось найти фильм с ID {} ", filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти фильм с ID " + filmId);
        }
    }
}
