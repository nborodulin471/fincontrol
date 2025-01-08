package ru.fincontrol.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fincontrol.model.dto.BalanceDto;
import ru.fincontrol.model.entity.Category;
import ru.fincontrol.model.OperationType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BalanceService sut;

    @Test
    @DisplayName("Корректно сформировали остатки когда вернулись категории")
    void showBalance_correctBalance() {
        // Given
        var category1 = getCategory("Продукты", 100);
        var category2 = getCategory("Транспорт", 200);
        var categories = Arrays.asList(category1, category2);
        when(categoryService.findAllForWallet()).thenReturn(categories);
        when(categoryService.getCurrentSumByCategory(category1, OperationType.EXPENSE)).thenReturn(BigDecimal.valueOf(50));
        when(categoryService.getCurrentSumByCategory(category2, OperationType.EXPENSE)).thenReturn(BigDecimal.valueOf(100));

        // When
        var result = sut.showBalance();

        // Then
        assertEquals(2, result.size());
        BalanceDto balanceDto1 = result.get(0);
        assertEquals("Продукты", balanceDto1.getCategoryName());
        assertEquals(BigDecimal.valueOf(100), balanceDto1.getLimit());
        assertEquals(BigDecimal.valueOf(50), balanceDto1.getExpenses());
        assertEquals(BigDecimal.valueOf(50), balanceDto1.getBalance());
        BalanceDto balanceDto2 = result.get(1);
        assertEquals("Транспорт", balanceDto2.getCategoryName());
        assertEquals(BigDecimal.valueOf(200), balanceDto2.getLimit());
        assertEquals(BigDecimal.valueOf(100), balanceDto2.getExpenses());
        assertEquals(BigDecimal.valueOf(100), balanceDto2.getBalance());
    }

    @Test
    @DisplayName("Обработали ситуацию когда получили пустой список категорий")
    void showBalance_emptyList() {
        // Given
        when(categoryService.findAllForWallet()).thenReturn(List.of());

        // When
        var result = sut.showBalance();

        // Then
        assertEquals(0, result.size());
    }

    private Category getCategory(String name, int val) {
        var category = new Category();
        category.setName(name);
        category.setLimiter(BigDecimal.valueOf(val));

        return category;
    }
}