package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@Slf4j
public class User {
    private Long id;
    private String name;
    private String email;
}
