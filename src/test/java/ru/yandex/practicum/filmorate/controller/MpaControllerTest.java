package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MpaControllerTest {

    private MpaController mpaController;
    private MpaService mpaService;

    @BeforeEach
    void setUp() {
        // Arrange
        mpaService = new MpaService(null); // В реальном тесте будет зависимость
        mpaController = new MpaController(mpaService);
    }

    @Test
    void mpaController_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(mpaController);
    }

    @Test
    void mpaService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(mpaService);
    }

    @Test
    void testMpa_ShouldBeCreated() {
        // Arrange
        Mpa mpa = new Mpa(1, "G");

        // Act & Assert
        assertNotNull(mpa);
        assertEquals(1, mpa.getId());
        assertEquals("G", mpa.getName());
    }
}
