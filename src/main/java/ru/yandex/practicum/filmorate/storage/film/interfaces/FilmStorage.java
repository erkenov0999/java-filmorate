package ru.yandex.practicum.filmorate.storage.film.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addNewFilm(Film film);
    Film updateFilm(Film film);
    void deleteFilm(Film film);
    List<Film> getAllFilms();
    Film getFilmById(long id);
}
