package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.friends.dao.FriendsDbStorage;

import java.util.List;

@AllArgsConstructor
@Service
public class FriendsService {
    private final FriendsDbStorage friendsStorage;

    public void addFriend(Long userId, Long friendId) {
        friendsStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        friendsStorage.removeFriend(userId, friendId);
    }

    public List<Long> getFriendsIdByUserId(long userId) {
        return friendsStorage.getFriendsIdByUserId(userId);
    }
}
