package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepository {
    public Map<Long, User> userStorage = new HashMap<>();

    public Long userId = 0L;

    public Long genId() {
        return ++userId;
    }

    public User add(@Valid User user) {
        validate(user);
        user.setId(genId());
        userStorage.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    public User getUserById(Long userId) {
        checkUserId(userId);
        return userStorage.get(userId);
    }

    public void deleteUser(Long id) {
        userStorage.remove(id);
    }

    public List<User> getAllUser() {
        return new ArrayList<>(userStorage.values());
    }

    private void validate(User user) {
        if (user.getName().isBlank()) {
            throw new ValidationException("Имя у пользователя не может быть пустым");
        }
        if (user.getEmail() == null) {

            throw new BadRequestException("Email у пользователя не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {

            throw new BadRequestException("Email у пользователя не имеет @");
        }
        checkEmailUser(user.getEmail());
    }

    private void checkEmailUser(String email) {
        if (userStorage.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new ValidationException("Такой email занят другим пользователем");
        }
    }

    private void checkUserId(Long userId) {
        if (!userStorage.containsKey(userId)) {
            throw new BadRequestException("Пользователь с id = " + userId + " не найден");
        }
    }
}
