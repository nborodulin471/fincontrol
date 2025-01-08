package ru.fincontrol.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fincontrol.dao.OperationRepository;
import ru.fincontrol.model.entity.Category;
import ru.fincontrol.model.entity.Operation;
import ru.fincontrol.model.OperationType;
import ru.fincontrol.model.entity.User;
import ru.fincontrol.model.entity.Wallet;
import ru.fincontrol.service.exception.ExceededExpensesException;
import ru.fincontrol.service.exception.LimitException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    private AuthFetcher authFetcher;
    @Mock
    private OperationRepository operationRepository;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private OperationService sut;

    private User user;

    @BeforeEach
    void setUp() {
        var wallet = new Wallet();
        wallet.setOperations(List.of());
        user = new User();
        user.setWallet(wallet);
    }

    @Test
    @DisplayName("Успешно добавили операцию")
    void addOperation_shouldSaveOperation_whenLimitNotExceeded() {
        // Given
        var categoryId = 1L;
        var amount = BigDecimal.valueOf(50);
        var category = getCategory(categoryId);
        var operation = getOperation(OperationType.PROFIT, 100, category);
        user.getWallet().setOperations(List.of(operation));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);
        when(categoryService.fetchCategory(categoryId)).thenReturn(category);
        when(categoryService.getCurrentSumByCategory(any(), any())).thenReturn(BigDecimal.valueOf(30));

        // When
        sut.addOperation(categoryId, amount, "Расход");

        // Then
        verify(operationRepository).save(any(Operation.class));
    }

    @Test
    @DisplayName("Получили ошибку если превысили лимит")
    void addOperation_shouldThrowLimitException_whenLimitExceeded() {
        // Given
        long categoryId = 1L;
        var amount = BigDecimal.valueOf(100);
        var category = getCategory(categoryId);
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);
        when(categoryService.fetchCategory(categoryId)).thenReturn(category);
        when(categoryService.getCurrentSumByCategory(any(), any())).thenReturn(BigDecimal.valueOf(100));

        // When & Then
        assertThrows(LimitException.class, () -> sut.addOperation(categoryId, amount, "Расход"));
    }

    @Test
    @DisplayName("Получили исключение, если расходы превысили доходы")
    void addOperation_shouldThrowExceededExpensesException_whenExpensesExceedProfit() {
        // Given
        var amount = BigDecimal.valueOf(50);
        var categoryId = 1L;
        var category = getCategory(1L);
        var operation1 = getOperation(OperationType.EXPENSE, 100, category);
        var operation2 = getOperation(OperationType.PROFIT, 100, category);
        user.getWallet().setOperations(Arrays.asList(operation1, operation2));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);
        when(categoryService.fetchCategory(categoryId)).thenReturn(category);
        when(categoryService.getCurrentSumByCategory(any(), any())).thenReturn(BigDecimal.valueOf(30));
        when(operationRepository.save(any(Operation.class))).thenReturn(new Operation());

        // When & Then
        assertThrows(ExceededExpensesException.class, () -> sut.addOperation(categoryId, amount, "Расход"));
    }

    @Test
    @DisplayName("Корректно получили операции")
    void getOperation_shouldReturnOperations() {
        // Given
        var operation1 = new Operation();
        var operation2 = new Operation();
        user.getWallet().setOperations(Arrays.asList(operation1, operation2));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);

        // When
        var result = sut.getOperation();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Получили правильную сумму по операциям")
    void getCurrentSum_shouldReturnCorrectSum() {
        // Given
        var operation1 = getOperation(OperationType.EXPENSE, 50, getCategory(1L));
        var operation2 = getOperation(OperationType.PROFIT, 100, getCategory(2L));
        user.getWallet().setOperations(Arrays.asList(operation1, operation2));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);

        // When
        var resultExpense = sut.getCurrentSum(OperationType.EXPENSE);
        var resultProfit = sut.getCurrentSum(OperationType.PROFIT);

        // Then
        assertEquals(BigDecimal.valueOf(50), resultExpense);
        assertEquals(BigDecimal.valueOf(100), resultProfit);
    }

    @Test
    @DisplayName("Удалили операцию, даже если ее нет")
    void deleteOperation_ShouldCallDeleteById() {
        // Given
        var operationId = 1L;

        // When
        sut.deleteOperation(operationId);

        // Then
        verify(operationRepository).deleteById(operationId);
    }

    private static Operation getOperation(OperationType type, int val, Category category) {
        var operation = new Operation();
        operation.setOperationType(type);
        operation.setAmount(BigDecimal.valueOf(val));
        operation.setCategory(category);

        return operation;
    }

    private static Category getCategory(long categoryId) {
        var category = new Category();
        category.setId(categoryId);
        category.setLimiter(BigDecimal.valueOf(100));
        return category;
    }
}
