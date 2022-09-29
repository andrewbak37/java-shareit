package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        validation(userId, itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userRepository.getUserById(userId).getId());
        return ItemMapper.itemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(long userId, Long itemId, ItemDto itemDto) {
        if (userRepository.getUserById(userId) == null
                || itemRepository.getItemById(itemId) == null
                || !itemRepository.getItemById(itemId).getOwnerId().equals(userId)) {
            throw new NotFoundException("Вы пытаетесь изменить чужую вещь");
        }
        Item item = itemRepository.getItemById(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.itemDto(itemRepository.update(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        return ItemMapper.itemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Нет такого юзера");
        }
        return itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(ItemMapper::itemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByText(String text) {
        if (text.equals("")) {
            return new ArrayList<>();
        }
        Predicate<Item> inName = item -> item.getName().toLowerCase().contains(text.toLowerCase());
        Predicate<Item> inDesc = item -> item.getDescription().toLowerCase().contains(text.toLowerCase());

        return itemRepository.getAllItems()
                .stream()
                .filter(inName.or(inDesc))
                .filter(Item::getAvailable)
                .map(ItemMapper::itemDto)
                .collect(Collectors.toList());
    }

    public void validation(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        boolean b = userRepository.userStorage.containsKey(userId);
        if (!b) {
            throw new NotFoundException("404");
        }

        if (item.getName().equals("")) {
            throw new BadRequestException("400");
        }
        if (item.getDescription() == null) {
            throw new BadRequestException("400");
        }

        if (item.getAvailable() == null) {
            throw new BadRequestException("400");
        }
    }
}
