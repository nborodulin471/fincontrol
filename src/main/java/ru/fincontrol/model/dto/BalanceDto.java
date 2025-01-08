package ru.fincontrol.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Модель ддя отражения остатков по категориям.
 *
 * @author Бородулин Никита Петрович.
 */
@Data
@Builder
public class BalanceDto {

    private String categoryName;
    private BigDecimal expenses;
    private BigDecimal limit;
    private BigDecimal balance;
}

