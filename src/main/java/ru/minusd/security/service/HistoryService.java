package ru.minusd.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.minusd.security.domain.dto.HistoryUserDTO;
import ru.minusd.security.domain.model.History;
import ru.minusd.security.domain.model.Item;
import ru.minusd.security.domain.model.User;
import ru.minusd.security.repository.HistoryRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UserService userService;
    private final ItemService itemService;

    public boolean buy(String username, Long itemId, Integer amount){
        User user = userService.getByUsername(username);
        Item item = itemService.findById(itemId).orElse(null);
        if(user.getMoney() < (item.getPrice() * amount)) return false;
        historyRepository.save(new History(user, item, amount));
        user.setMoney(user.getMoney() - amount * item.getPrice());
        userService.save(user);
        return true;
    }

    public List<HistoryUserDTO> getHistoryUser(String username){
        return historyRepository.findByUser(
                userService.getByUsername(username)
                )
                .stream()
                .map(purchase -> new HistoryUserDTO(
                        purchase.getId(),
                        purchase.getItem().getId(),
                        purchase.getAmount()
                ))
                .collect(Collectors.toList());
    }


}
