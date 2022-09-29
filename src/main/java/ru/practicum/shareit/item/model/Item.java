package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Getter
@Setter
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}
