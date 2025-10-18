package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    private static RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> {
            User user = new User(
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate()
            );
            user.setId(rs.getLong("id"));
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
        User checkUser = checkAndFillName(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("users").
                usingGeneratedKeyColumns("id");
        Long id = simpleJdbcInsert.executeAndReturnKey(userToMap(checkUser)).longValue();
        checkUser.setId(id);
        return checkUser;
    }

    @Override
    public User updateUser(User user) throws ResponseStatusException {
        String request = "UPDATE users " +
                        "SET email = ?, " +
                        "login = ? " +
                        "name = ? " +
                        "birthday = ? " +
                        "WHERE id = ?";

        Map<String, Object> userToUpdate = userToMap(user);

        jdbcTemplate.update(request,
                userToUpdate.get("email"),
                userToUpdate.get("login"),
                userToUpdate.get("name"),
                userToUpdate.get("birthday"),
                user.getId()
        );

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
        return jdbcTemplate.query("SELECT * FROM users", getUserMapper());
    }

    @Override
    public Optional<User> findUserById(long id) throws ResponseStatusException {
        String request = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(request, getUserMapper(), id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public User checkAndFillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}
