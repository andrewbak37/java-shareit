package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.UserRepositoryMemory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserServiceMemImpl implements UserService {
    private final UserRepositoryMemory userRepositoryMemory;


    @Override
    public UserDto createUser(UserDto userDto) {
        checkEmail(userDto);
        User user = userRepositoryMemory.add(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userRepositoryMemory.getUserById(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkEmail(userDto);
            user.setEmail(userDto.getEmail());
        }
        userRepositoryMemory.updateUser(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepositoryMemory.deleteUser(id);
    }

    @Override
    public UserDto getUserById(long id) {
        return UserMapper.toUserDto(userRepositoryMemory.getUserById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> collect = userRepositoryMemory.getAllUser()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return collect;
    }

    public void checkEmail(UserDto userDto) {
        if (userRepositoryMemory.getAllUser()
                .stream()
                .anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new NotUniqueEmailException("email не уникальный");
        }
    }
}
