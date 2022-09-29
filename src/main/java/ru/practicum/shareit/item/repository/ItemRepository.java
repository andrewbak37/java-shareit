package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemRepository {
    public Map<Long, Item> items = new HashMap<>();
    private Long itemId = 0L;

    private Long getItemId() {
        return ++itemId;
    }

    public Item create(Item item) {
        item.setId(getItemId());
        items.put(item.getId(), item);
        return item;
    }

    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
}
