package ru.minusd.security.controller;

import io.micrometer.common.lang.NonNull;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.minusd.security.domain.dto.MoneyDTO;
import ru.minusd.security.domain.model.Item;
import ru.minusd.security.service.ItemService;
import ru.minusd.security.service.JwtService;
import ru.minusd.security.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ItemService itemService;
    @PostMapping("/sendMoney")
    public ResponseEntity<String> sendMoney(@RequestBody MoneyDTO moneyDTO, @NonNull Principal principal){
        if(userService.sendMoney(principal.getName(), moneyDTO.getUsername(), moneyDTO.getMoney()))
            return new ResponseEntity<>("All Ok", HttpStatus.OK);
        return new ResponseEntity<>("Error: no have money.", HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/giveMoney")
    public void giveMoney(@RequestBody MoneyDTO moneyDTO){
        userService.giveMoney(moneyDTO.getUsername(), moneyDTO.getMoney());
    }

    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/setDefaultItems")
    public void setDefaultItems(){
        List<Item> items = new ArrayList<>();
        items.add(new Item("t-shirt", 80));
        items.add(new Item("cup", 20));
        items.add(new Item("book", 50));
        items.add(new Item("pen", 10));
        items.add(new Item("powerbank", 200));
        items.add(new Item("hoody", 300));
        items.add(new Item("umbrella", 200));
        items.add(new Item("socks", 10));
        items.add(new Item("wallet", 50));
        items.add(new Item("pink-hoody", 500));
        for(Item item: items){
            itemService.create(item.getName(), item.getPrice());
        }
    }
}
