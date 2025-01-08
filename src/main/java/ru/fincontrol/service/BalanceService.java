package ru.fincontrol.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.model.dto.BalanceDto;
import ru.fincontrol.model.OperationType;

import java.util.ArrayList;
import java.util.List;


/**
 * Сервис, который отвечает за работу с остатками по категориям.
 *
 * @author Бородулин Никита Петрович.
 */
@Service
@RequiredArgsConstructor
public class BalanceService {

    private final CategoryService categoryService;

    /**
     * Метод, который формирует информацию по остаткам.
     */
    public List<BalanceDto> showBalance() {
        var categories = categoryService.findAllForWallet();
        var result = new ArrayList<BalanceDto>();
        categories.forEach(category -> {
            var expenses = categoryService.getCurrentSumByCategory(category, OperationType.EXPENSE);
            var balanceDto = BalanceDto.builder()
                    .categoryName(category.getName())
                    .limit(category.getLimiter())
                    .expenses(expenses)
                    .balance(category.getLimiter().subtract(expenses))
                    .build();

            result.add(balanceDto);
        });

        return result;
    }
}
