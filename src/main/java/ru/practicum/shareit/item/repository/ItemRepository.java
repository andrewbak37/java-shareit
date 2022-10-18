package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE (LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%) AND i.available = true")
    List<Item> searchByKeyword(String text);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.owner.id = :userId " +
            "ORDER BY i.id")
    List<Item> findAllById(Long userId);
}
