package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.interfaces.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsService friendsService;

    public User addNewUser(User newUser) {
        checkAndFillName(newUser);
        return userStorage.addNewUser(newUser);
    }

    public User updateUser(User user) {
        Long userId = user.getId();
        boolean userExist = userStorage.isUserExists(userId);
        if (!userExist) {
            log.info("Пользователь для обновления с ID {} не найден!", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Пользователь для обновления не найден ID = " + userId);
        }

        checkAndFillName(user);
        User updatedUser = userStorage.updateUser(user);

        Set<Long> friends = user.getFriends();
        removeAllFriends(userId, friends);
        addFriends(userId, friends);
        return updatedUser;
    }

    public void addFriend(long userId, long friendId) throws ResponseStatusException {
        checkingBeforeAddingFriends(userId, friendId);

        Optional<User> userOpt = userStorage.findUserById(userId);
        Optional<User> friendOpt = userStorage.findUserById(friendId);
        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        User user = userOpt.get();
        User friend = friendOpt.get();

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Пользователи {} и {} добавили друг друга в друзья", friendId, userId);
    }

    public void addFriends(long userId, Set<Long> friendIds) throws ResponseStatusException {
        for (Long friendId : friendIds) {
            addFriend(userId, friendId);
        }
    }

    public void removeFriend(long id, long friendId) throws ResponseStatusException {
        checkingBeforeAddingFriends(id, friendId);

        Optional<User> userOpt = userStorage.findUserById(id);
        Optional<User> friendOpt = userStorage.findUserById(friendId);
        
        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        User user = userOpt.get();
        User friend = friendOpt.get();
        user.getFriends().remove(friendId);
        friend.getFriends().remove(user.getId());
        
        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        log.info("Пользователь {} удалил из друзей {}", friendId, user.getId());
    }

    public void removeAllFriends(long userId, Set<Long> friendIds) throws ResponseStatusException {
        for (Long friendId : friendIds) {
            removeFriend(userId, friendId);
        }
    }

    public Set<User> getFriends(long userId) throws ResponseStatusException {
        Optional<User> userOpt = userStorage.findUserById(userId);
        if (userOpt.isEmpty()) {
            log.info("Пользователь с переданным ID={} не найден", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с переданным ID не найден");
        }

        User user = userOpt.get();
        Set<User> friends = new HashSet<>();
        
        for (long friendId : user.getFriends()) {
            Optional<User> friendOpt = userStorage.findUserById(friendId);
            friendOpt.ifPresent(friends::add);
        }
        return friends;
    }

    public Set<User> getCommonFriends(long firstUserId, long secondUserId) throws ResponseStatusException {
        checkingBeforeAddingFriends(firstUserId, secondUserId);

        Optional<User> firstUserOpt = userStorage.findUserById(firstUserId);
        Optional<User> secondUserOpt = userStorage.findUserById(secondUserId);
        
        if (firstUserOpt.isEmpty() || secondUserOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        
        User firstUser = firstUserOpt.get();
        User secondUser = secondUserOpt.get();
        Set<Long> commonFriends = new HashSet<>(secondUser.getFriends());
        commonFriends.retainAll(firstUser.getFriends());

        Set<User> friends = new HashSet<>();
        for (long friendId : commonFriends) {
            Optional<User> friendOpt = userStorage.findUserById(friendId);
            friendOpt.ifPresent(friends::add);
        }
        
        log.info("Вывод списка общих друзей между пользователям {} и пользователем {}", firstUserId, secondUserId);
        return friends;
    }

    private void checkingBeforeAddingFriends(long userId, long friendId) throws ResponseStatusException {
        if (userStorage.findUserById(userId).isEmpty()) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + userId + " для добавления/удаления друга.");
        }

        if (userStorage.findUserById(friendId).isEmpty()) {
            log.error("Не удалось найти пользователя с ID {} для добавления/удаления друга.", friendId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не удалось найти пользователя с ID "
                    + friendId + " для добавления/удаления друга.");
        }

        if (userId == friendId) {
            log.error("Запрещено добавлять в друзья или удалять самого себя {}", friendId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Запрещено выполнять это действие с самим собой.");
        }
    }

    public void deleteUser(User user) {
        log.info("Удаление пользователя с ID: {}", user.getId());
        userStorage.deleteUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userStorage.findAllUsers();
    }

    public User getUserById(long id) {
        log.info("Получение пользователя с ID: {}", id);
        return userStorage.findUserById(id).orElse(null);
    }

    public User checkAndFillName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user;
        }
        return user;
    }
}