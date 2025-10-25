package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.storage.mpa.dao.MpaDbStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MpaServiceTest {

    private MpaService mpaService;
    private MpaDbStorage mpaDbStorage;

    @BeforeEach
    void setUp() {
        // Arrange
        mpaDbStorage = new MpaDbStorage(null); // В реальном тесте будет JdbcTemplate
        mpaService = new MpaService(mpaDbStorage);
    }

    @Test
    void mpaService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(mpaService);
    }

    @Test
    void mpaDbStorage_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(mpaDbStorage);
    }
}