package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    private static RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        );
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
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", getUserMapper());
    }

    @Override
    public User findUserById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", getUserMapper(), id);
    }

    public User checkAndFillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}
