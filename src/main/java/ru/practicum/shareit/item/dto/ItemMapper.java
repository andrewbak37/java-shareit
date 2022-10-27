package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemInformDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(null)
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static ItemInfoDto toItemInfoDto(Item item,
                                            Booking lastBooking,
                                            Booking nextBooking,
                                            List<Comment> comments) {
        return new ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                ItemInfoDto.BookingInfoDto.create(lastBooking),
                ItemInfoDto.BookingInfoDto.create(nextBooking),
                CommentMapper.mapToCommentInfoDto(comments)
        );
    }

    public static Item toItem(ItemDto itemDto,
                              User user,
                              ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        item.setRequest(itemRequest);

        return item;
    }

    public static ItemInformDto itemInformDto(Item item) {
        ItemInformDto itemInformDto = new ItemInformDto();
        itemInformDto.setId(item.getId());
        itemInformDto.setName(item.getName());
        itemInformDto.setDescription(item.getDescription());
        itemInformDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemInformDto.setRequestId(item.getRequest().getId());
        }
        return itemInformDto;
    }
}