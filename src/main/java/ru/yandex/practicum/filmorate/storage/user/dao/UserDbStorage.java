package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.friends.interfaces.FriendsStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendsStorage friendsStorage;

    private RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> {
            User user = new User(
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate()
            );
            user.setId(rs.getLong("id"));
            // Загружаем друзей для пользователя
            user.getFriends().addAll(friendsStorage.getFriendsIdByUserId(user.getId()));
            return user;
        };
    }

    private static Map<String, Object> userToMap(User user) {
        return Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday()
        );
    }

    @Override
    public User addNewUser(User user) {
        log.info("Добавление нового пользователя: {}", user.getLogin());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Long id = simpleJdbcInsert.executeAndReturnKey(userToMap(user))
                .longValue();
        user.setId(id);
        log.info("Пользователь успешно добавлен с ID: {}", id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя с ID: {}", user.getId());
        String request = "UPDATE users " +
                        "SET email = ?, " +
                        "login = ?, " +
                        "name = ?, " +
                        "birthday = ? " +
                        "WHERE id = ?";
        Map<String, Object> userToUpdate = userToMap(user);
        int rowsAffected = jdbcTemplate.update(request,
                userToUpdate.get("email"),
                userToUpdate.get("login"),
                userToUpdate.get("name"),
                userToUpdate.get("birthday"),
                user.getId()
        );
        if (rowsAffected == 0) {
            log.warn("Пользователь с ID {} не найден для обновления", user.getId());
        } else {
            log.info("Пользователь с ID {} успешно обновлен", user.getId());
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        deleteUserById(user.getId());
    }

    private void deleteUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            log.warn("Пользователь с id {} не найден для удаления", id);
        } else {
            log.info("Пользователь с id {} успешно удален", id);
        }
    }

    @Override
    public List<User> findAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = jdbcTemplate.query("SELECT * FROM users", getUserMapper());
        log.info("Найдено {} пользователей", users.size());
        return users;
    }

    @Override
    public Optional<User> findUserById(long id) {
        log.info("Получение пользователя с ID: {}", id);
        String request = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(request, getUserMapper(), id);
            log.info("Пользователь с ID {} найден", id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Пользователь с ID {} не найден", id);
            return Optional.empty();
        }
    }

    public boolean isUserExists(Long userId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, userId);
    }
}
