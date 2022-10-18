package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "data_start")
    private LocalDateTime start;

    @Column(name = "data_end")
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "booker_id")
    public User booker;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

}
