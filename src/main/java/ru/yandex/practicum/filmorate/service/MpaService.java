package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.interfaces.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> findAll() {
        log.info("Получение списка всех рейтингов MPA");
        return mpaStorage.findAll();
    }

    public Optional<Mpa> findById(int mpaId) {
        log.info("Получение рейтинга MPA с ID {}", mpaId);
        return mpaStorage.findById(mpaId);
    }
}


