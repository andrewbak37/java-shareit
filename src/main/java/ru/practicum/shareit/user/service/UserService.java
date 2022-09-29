package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUser(long id);

    UserDto getUserById(long id);

    List<UserDto> getAllUsers();
}
