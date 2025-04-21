package ru.minusd.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryUserDTO {
    Long purchaseId;
    Long itemId;
    Integer amount;
}
