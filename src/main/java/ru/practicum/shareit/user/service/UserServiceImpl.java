package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        checkEmail(userDto);
        User user = userRepository.add(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userRepository.getUserById(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkEmail(userDto);
            user.setEmail(userDto.getEmail());
        }
        userRepository.updateUser(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public UserDto getUserById(long id) {
        return UserMapper.toUserDto(userRepository.getUserById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> collect = userRepository.getAllUser()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return collect;
    }

    public void checkEmail(UserDto userDto) {
        if (userRepository.getAllUser()
                .stream()
                .anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new NotUniqueEmailException("email не уникальный");
        }
    }
}
