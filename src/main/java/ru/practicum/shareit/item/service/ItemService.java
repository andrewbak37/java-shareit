package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(long userId, Long itemId, ItemDto itemDto);

    ItemInfoDto getItemById(Long itemId, Long userId);

    List<ItemInfoDto> getAllItemsByUserId(Long userId);

    List<ItemDto> findItemByText(String text);

    CommentInfoDto createComment(CommentDto commentDto, Long userId, Long itemId);


}
