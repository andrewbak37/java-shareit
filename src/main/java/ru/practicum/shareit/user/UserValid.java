package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserValid {
    public void validation(UserDto userDto) {

        if (userDto.getEmail() == null) {
            throw new BadRequestException("400");
        }

        if (userDto.getEmail().equals("user.com")) {
            throw new BadRequestException("400");
        }
    }
}
