package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemInformDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ItemServiceImplTest {

    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository
        );
    }

    @Test
    void shouldThrowExceptionUsingSaveWhenWrongUserId() {
        ItemDto itemDto = new ItemDto(1L, "test", "test", true, null, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.create(1L, itemDto));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionUsingSaveWhenWrongRequestId() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemDto itemDto = new ItemDto(null, "test", "test", true, 1L, null);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> itemService.create(1L, itemDto));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldSaveCommentWithRightParameters() {
        CommentDto commentDto = new CommentDto(null, "test");
        CommentInfoDto commentInfoDto = new CommentInfoDto(1L, "test", "test", LocalDateTime.now());
        User user = new User(1L, "test", "test@gmail.com");
        Item item = new Item(1L, user, "test", "test", true, null);
        Comment comment = new Comment(1L, "test", user, item, LocalDateTime.now());

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito.when(bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(
                        Mockito.anyLong(),
                        Mockito.anyLong(),
                        Mockito.any(LocalDateTime.class)))
                .thenReturn(true);

        CommentInfoDto foundComment = itemService.createComment(commentDto, 1L, 1L);

        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(commentInfoDto.getId(), foundComment.getId());
        Assertions.assertEquals(commentInfoDto.getText(), foundComment.getText());
        Assertions.assertEquals(commentInfoDto.getAuthorName(), foundComment.getAuthorName());
    }

    @Test
    void shouldReturnEmptyListUsingSearch() {
        User user = new User(1L, "test", "test@gmail.com");
        String text = "";

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));

        List<ItemDto> results = itemService.findItemByText(text, user.getId(), 0, 10);

        Assertions.assertEquals(0, results.size());
    }
}