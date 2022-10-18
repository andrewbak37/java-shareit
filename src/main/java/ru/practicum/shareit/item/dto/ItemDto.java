package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.Create;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Setter
@Getter

public class ItemDto {
    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String description;

    @NotBlank(groups = {Create.class})
    private Boolean available;

    private Long owner;
}
