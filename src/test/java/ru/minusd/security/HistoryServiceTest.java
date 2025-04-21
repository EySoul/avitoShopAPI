package ru.minusd.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.minusd.security.domain.dto.HistoryUserDTO;
import ru.minusd.security.domain.model.History;
import ru.minusd.security.domain.model.Item;
import ru.minusd.security.domain.model.User;
import ru.minusd.security.repository.HistoryRepository;
import ru.minusd.security.service.HistoryService;
import ru.minusd.security.service.ItemService;
import ru.minusd.security.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class HistoryServiceTest {

    private HistoryRepository historyRepository;
    private UserService userService;
    private ItemService itemService;
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        historyRepository = mock(HistoryRepository.class);
        userService = mock(UserService.class);
        itemService = mock(ItemService.class);
        historyService = new HistoryService(historyRepository, userService, itemService);
    }

    @Test
    public void testBuy_Success() {
        // Подготовка данных
        String username = "testUser";
        Long itemId = 1L;
        Integer amount = 2;

        User user = new User();
        user.setUsername(username);
        user.setMoney(200); // У пользователя достаточно денег


        Item item = new Item();
        item.setId(itemId);
        item.setPrice(50); // Цена товара

        // Настройка моков
        when(userService.getByUsername(username)).thenReturn(user);
        when(itemService.findById(itemId)).thenReturn(Optional.of(item));

        // Вызов метода
        historyService.buy(username, itemId, amount);

        // Проверка, что история покупки была сохранена
        verify(historyRepository).save(any(History.class));

        // Проверка, что деньги у пользователя были вычтены
        assertEquals(100, user.getMoney()); // 200 - (50 * 2) = 100
    }

    @Test
    public void testBuy_InsufficientFunds() {
        // Подготовка данных
        String username = "testUser";
        Long itemId = 1L;
        Integer amount = 3;

        User user = new User();
        user.setUsername(username);
        user.setMoney(100); // У пользователя недостаточно денег

        Item item = new Item();
        item.setId(itemId);
        item.setPrice(50); // Цена товара

        // Настройка моков
        when(userService.getByUsername(username)).thenReturn(user);
        when(itemService.findById(itemId)).thenReturn(Optional.of(item));

        // Вызов метода
        historyService.buy(username, itemId, amount);

        // Проверка, что история покупки не была сохранена
        verify(historyRepository, never()).save(any(History.class));

        // Проверка, что деньги у пользователя не изменились
        assertEquals(100, user.getMoney()); // Осталось 100
    }

    @Test
    public void testGetHistoryUser_Success() {
        // Подготовка данных
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        History history1 = new History();
        history1.setId(1L);
        Item item1 = new Item();
        item1.setId(101L);
        history1.setItem(item1);
        history1.setAmount(2);

        History history2 = new History();
        history2.setId(2L);
        Item item2 = new Item();
        item2.setId(102L);
        history2.setItem(item2);
        history2.setAmount(1);

        // Настройка моков
        when(userService.getByUsername(username)).thenReturn(user);
        when(historyRepository.findByUser(user)).thenReturn(Arrays.asList(history1, history2));

        // Вызов метода
        List<HistoryUserDTO> historyDTOs = historyService.getHistoryUser(username);

        // Проверка результатов
        assertEquals(2, historyDTOs.size());
        assertEquals(1L, historyDTOs.get(0).getPurchaseId());
        assertEquals(101L, historyDTOs.get(0).getItemId());
        assertEquals(2, historyDTOs.get(0).getAmount());
        assertEquals(2L, historyDTOs.get(1).getPurchaseId());
        assertEquals(102L, historyDTOs.get(1).getItemId());
        assertEquals(1, historyDTOs.get(1).getAmount());

        // Проверка, что методы были вызваны
        verify(userService).getByUsername(username);
        verify(historyRepository).findByUser(user);
    }


    @Test
    public void testGetHistoryUser_UserNotFound() {
        // Подготовка данных
        String username = "nonExistentUser";

        // Настройка моков
        when(userService.getByUsername(username)).thenReturn(null); // Пользователь не найден

        // Вызов метода
        List<HistoryUserDTO> historyDTOs = historyService.getHistoryUser(username);

        // Проверка результатов
        assertTrue(historyDTOs.isEmpty()); // Ожидаем пустой список

        // Проверка, что метод findByUser не был вызван
        verify(historyRepository, never()).findByUser(any(User.class));
    }
}
