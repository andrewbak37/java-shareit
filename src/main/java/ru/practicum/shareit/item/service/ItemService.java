package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.request.dto.ItemInformDto;

import java.util.List;

public interface ItemService {

    ItemInformDto create(Long userId, ItemDto itemDto);

    ItemDto update(long userId, Long itemId, ItemDto itemDto);

    ItemInfoDto getItemById(Long itemId, Long userId);

    List<ItemInfoDto> getAllItemsByUserId(Long userId, int from, int size);

    List<ItemDto> findItemByText(String text, Long userId, int from, int size);

    CommentInfoDto createComment(CommentDto commentDto, Long userId, Long itemId);


}
