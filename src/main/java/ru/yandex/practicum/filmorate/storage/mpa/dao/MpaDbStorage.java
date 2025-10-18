package ru.yandex.practicum.filmorate.storage.mpa.dao;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.interfaces.MpaStorage;

import java.util.List;
import java.util.Optional;

public class MpaDbStorage implements MpaStorage {

    @Override
    public List<Mpa> findAll() {
        return List.of();
    }

    @Override
    public Optional<Mpa> findById(int mpaId) {
        return Optional.empty();
    }
}
