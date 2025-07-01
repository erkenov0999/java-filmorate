package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final Set<Film> topFilms = new TreeSet<>(
            Comparator.comparingInt((Film film) -> -film.getLikes().size())
                    .thenComparing(Film::getId));

    public static final LocalDate RELEASE_DATE_LOWER_BOUND = LocalDate.of(1895, 12, 28);
    private long idCounter = 1;

    public Film releaseDateValidation(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE_LOWER_BOUND)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        return film;
    }

    public Long generateId() {
        return idCounter++;
    }

    public Long putLike(Film film, User user) {
        if(film.getLikes().contains(user.getId())) {
            throw new ValidationException("Пользователь под ником " + user.getName()
                    + " c id " + user.getId()
                    + " уже поставил лайк данному фильму.");
        }

        film.getLikes().add(user.getId());
        updateTopFilms(film);
        log.info("Пользователь с идентификатором " + user.getId()
                + " поставил лайк на фильм с id " + film.getId());

        return (long) film.getLikes().size();
    }

    public Long removeLike(Film film, User user) {
        if (!film.getLikes().contains(user.getId())) {
            throw new ValidationException("Убрать лайк нельзя, так как пользователь c id " + user.getId()
                    + " не ставил лайк на фильм с id " + film.getId());
        }

        film.getLikes().remove(user.getId());
        updateTopFilms(film);
        log.info("Пользователь " + user.getId() + " убрал лайк с фильма с id " + film.getId());

        return (long) film.getLikes().size();
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
        log.info("Запрошен топ-{} фильмов", limit);
        return topFilms.stream()
                .limit(limit)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
