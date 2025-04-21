package ru.minusd.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.minusd.security.domain.model.Item;
import ru.minusd.security.repository.ItemRepository;
import ru.minusd.security.service.ItemService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ItemServiceTest {

    private ItemRepository itemRepository;
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemService(itemRepository);
    }

    @Test
    public void testFindById_ItemExists() {
        // Подготовка данных
        Long itemId = 1L;
        Item expectedItem = new Item();
        expectedItem.setId(itemId);
        expectedItem.setName("Test Item");

        // Настройка мока
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        // Вызов метода
        Optional<Item> actualItem = itemService.findById(itemId);

        // Проверка результатов
        assertTrue(actualItem.isPresent());
        assertEquals(expectedItem, actualItem.get());

        // Проверка, что метод findById был вызван
        verify(itemRepository).findById(itemId);
    }
}
