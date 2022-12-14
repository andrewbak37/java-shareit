package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import java.util.List;

public interface BookingService {

    BookingInfoDto booking(BookingDto bookingDto, Long userId);

    BookingInfoDto approve(Long userId, Long bookingId, Boolean approved);

    BookingInfoDto getById(Long userId, Long bookingId);

    List<BookingInfoDto> getBookingBookerByState(Long userId, String state, int from, int size);

    List<BookingInfoDto> getBookingOwnerByState(Long userId, String state, int from, int size);
}
