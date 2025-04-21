package ru.minusd.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.minusd.security.domain.model.Item;
import ru.minusd.security.repository.ItemRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public void create(String name, Integer price){
        itemRepository.save(new Item(name, price));
    }

    public Optional<Item> findById(Long id){
        return itemRepository.findById(id);
    }
}
