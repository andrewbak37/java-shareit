package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping()
    ItemDto create(@NotBlank @RequestHeader("X-Sharer-User-Id") Long userId,
                   @RequestBody @Valid ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    ItemDto update(@NotBlank @RequestHeader("X-Sharer-User-Id") Long userId,
                   @PathVariable Long itemId,
                   @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping()
    List<ItemDto> getAllItemsByUser(@NotBlank @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("search")
    List<ItemDto> search(@RequestParam(required = false) String text) {
        return itemService.findItemByText(text);
    }
}
