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
import ru.yandex.practicum.filmorate.storage.mpa.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.storage.genres.interfaces.GenresStorage;

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
    private final MpaStorage mpaStorage;
    private final GenresStorage genresStorage;
    private List<Film> topFilms = new ArrayList<>();

    public Film addNewFilm(Film film) {
        log.info("Добавление нового фильма: {}", film.getName());
        // Валидируем MPA
        if (film.getMpa() != null && mpaStorage.findById(film.getMpa().getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MPA рейтинг с ID " + film.getMpa().getId() + " не найден");
        }
        // Валидируем жанры
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (var genre : film.getGenres()) {
                if (genresStorage.findById(genre.getId()).isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр с ID " + genre.getId() + " не найден");
                }
            }
        }
        Film savedFilm = filmStorage.addNewFilm(film);
        // Сохраняем жанры фильма
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenresStorage.deleteFilmGenresByFilmId(savedFilm.getId());
            film.getGenres().forEach(genre ->
                    filmGenresStorage.addFilmGenre(savedFilm.getId(), genre.getId())
            );
        }
        // Возвращаем фильм с загруженными жанрами из БД
        return filmStorage.getFilmById(savedFilm.getId()).orElse(savedFilm);
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма с ID: {}", film.getId());
        // Валидируем MPA
        if (film.getMpa() != null && mpaStorage.findById(film.getMpa().getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MPA рейтинг с ID " + film.getMpa().getId() + " не найден");
        }
        // Валидируем жанры
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (var genre : film.getGenres()) {
                if (genresStorage.findById(genre.getId()).isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр с ID " + genre.getId() + " не найден");
                }
            }
        }
        Film updatedFilm = filmStorage.updateFilm(film);
        // Проверяем, был ли фильм найден
        if (updatedFilm == null) {
            log.error("Фильм с ID {} не найден для обновления", film.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + film.getId() + " не найден");
        }
        // Обновляем жанры фильма
        if (film.getGenres() != null) {
            filmGenresStorage.deleteFilmGenresByFilmId(film.getId());
            film.getGenres().forEach(genre ->
                    filmGenresStorage.addFilmGenre(film.getId(), genre.getId())
            );
        }
        // Возвращаем фильм с загруженными жанрами из БД
        return filmStorage.getFilmById(film.getId()).orElse(updatedFilm);
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
        // Удаляем лайк, даже если его не было
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
        List<Film> listTopPopularFilms = new ArrayList<>();
        List<Film> allFilms = getFilmsSortedByPopularity();
        for (int i = 0; i < limit && i < allFilms.size(); i++) {
            listTopPopularFilms.add(allFilms.get(i));
        }
        return listTopPopularFilms;
    }

    private List<Film> getFilmsSortedByPopularity() throws ResponseStatusException {
        List<Film> allFilms = new ArrayList<>(filmStorage.getAllFilms());
        if (allFilms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Список фильмов пока еще пуст");
        }
        allFilms.sort(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed());
        return allFilms;
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
