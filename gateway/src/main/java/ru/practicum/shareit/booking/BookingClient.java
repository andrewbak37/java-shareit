package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, String stateParam, Integer from, Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getOwnerBookings(long userId, String stateParam, Integer from, Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> approveBooking(long userId, long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        // /bookings/5?approved=true
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        if (requestDto.getStart().isAfter(requestDto.getEnd())) {
            throw new ValidationException("Validation Failed");
        }
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }
}