package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        validation(item);
        item.setOwner(userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("NFE")));
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long userId, Long itemId, ItemDto itemDto) {
        checkUserExists(userId);
        Item updatedItem = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("NFE"));
        checkItemOwner(updatedItem.getOwner().getId(), userId);
        fillItem(updatedItem, itemDto);
        return ItemMapper.toDto(itemRepository.save(updatedItem));
    }

    @Override
    public ItemInfoDto getItemById(Long itemId, Long userId) {
        checkUserExists(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("NFE"));
        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        return ItemMapper.toItemInfoDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemInfoDto> getAllItemsByUserId(Long userId) {
        return itemRepository.findAllById(userId).stream()
                .map(item -> ItemMapper.toItemInfoDto(
                        item,
                        bookingRepository.findLastBooking(LocalDateTime.now(), userId, item.getId()),
                        bookingRepository.findNextBooking(LocalDateTime.now(), userId, item.getId()),
                        commentRepository.findCommentsByItemId(item.getId()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            text = text.toLowerCase();
            return itemRepository.searchByKeyword(text).stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CommentInfoDto createComment(CommentDto commentDto, Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("NFE"));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new ObjectNotFoundException("NFE"));
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new ValidationException("The user has not used this item yet");
        }
        Comment comment = CommentMapper.mapToComment(commentDto, user, item);
        return CommentMapper.mapToCommentInfoDto(commentRepository.save(comment));
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("NFE");
        }
    }

    private void checkItemOwner(Long ownerId, Long userId) {
        if (!ownerId.equals(userId)) {
            throw new NotFoundException("NFE");
        }
    }

    private void fillItem(Item item, ItemDto itemDto) {
        String name = itemDto.getName();
        if (name != null) {
            item.setName(name);
        }
        String description = itemDto.getDescription();
        if (description != null) {
            item.setDescription(description);
        }
        Boolean available = itemDto.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }
    }

    public void validation(Item item) {

        if (item.getAvailable() == null) {
            throw new BadRequestException("400");
        }
        if (item.getName().equals("")) {
            throw new BadRequestException("400");
        }
        if (item.getDescription() == null) {
            throw new BadRequestException("400");
        }
    }
}
