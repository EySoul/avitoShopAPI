package ru.minusd.security.controller;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.minusd.security.domain.dto.ByDTO;
import ru.minusd.security.domain.dto.HistoryUserDTO;
import ru.minusd.security.service.HistoryService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestBody ByDTO byDTO, @NonNull Principal principal){
        if(historyService.buy(principal.getName(), byDTO.getId(), byDTO.getAmount()))
            return new ResponseEntity<>("Success", HttpStatus.OK);;
        return new ResponseEntity<>("Error: no have money", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/buy/{id}")
    public ResponseEntity<String> buy(@PathVariable Long id, @NonNull Principal principal){
        if(historyService.buy(principal.getName(), id, 1))
            return new ResponseEntity<>("Success", HttpStatus.OK);;
        return new ResponseEntity<>("Error: no have money", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/info")
    @ResponseBody
    public List<HistoryUserDTO> getAll(@NonNull Principal principal){
        return historyService.getHistoryUser(principal.getName());
    }
}
