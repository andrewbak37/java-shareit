package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemInformDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping()
    public ItemInformDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long itemId) {
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{item_id}")
    public ItemDto update(@PathVariable("item_id") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping()
    public List<ItemInfoDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("search")
    public List<ItemDto> search(@RequestParam(name = "text", required = false) String text,
                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        return itemService.findItemByText(text, userId, from, size);
    }

    @PostMapping("/{item_id}/comment")
    public CommentInfoDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody CommentDto commentDto,
                                        @PathVariable("item_id") Long itemId) {
        return itemService.createComment(commentDto, userId, itemId);
    }
}
