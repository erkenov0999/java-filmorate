package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.friends.dao.FriendsDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendsServiceTest {

    private FriendsService friendsService;
    private FriendsDbStorage friendsDbStorage;

    @BeforeEach
    void setUp() {
        // Arrange
        friendsDbStorage = new FriendsDbStorage(null); // В реальном тесте будет JdbcTemplate
        friendsService = new FriendsService(friendsDbStorage);
    }

    @Test
    void friendsService_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(friendsService);
    }

    @Test
    void friendsDbStorage_ShouldBeCreated() {
        // Arrange
        // (Инициализация в setUp)

        // Act & Assert
        assertNotNull(friendsDbStorage);
    }
}
