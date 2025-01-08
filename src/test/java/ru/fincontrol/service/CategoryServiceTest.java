package ru.fincontrol.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fincontrol.dao.CategoryRepository;
import ru.fincontrol.model.entity.Category;
import ru.fincontrol.model.entity.Operation;
import ru.fincontrol.model.OperationType;
import ru.fincontrol.model.entity.User;
import ru.fincontrol.model.entity.Wallet;
import ru.fincontrol.service.exception.CategoryUsedException;
import ru.fincontrol.service.exception.NotFoundCategoryException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AuthFetcher authFetcher;

    @InjectMocks
    private CategoryService sut;

    private User user;

    @BeforeEach
    void setUp() {
        var wallet = new Wallet();
        user = new User();
        user.setWallet(wallet);
    }

    @Test
    @DisplayName("Успешно добавили категорию")
    void addCategory_shouldSaveCategory() {
        // Given
        var category = getCategory();
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        var result = sut.addCategory(category.getName(), category.getLimiter());

        // Then
        assertNotNull(result);
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getLimiter(), result.getLimiter());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @DisplayName("Обработали ситуацию когда пытаемся удалить категорию которой нет")
    void deleteCategory_shouldThrowNotFoundCategoryException_whenCategoryNotFound() {
        // Given
        var categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundCategoryException.class, () -> sut.deleteCategory(categoryId));
    }

    @Test
    @DisplayName("Не смогли удалить категорию которая используется")
    void deleteCategory_shouldThrowCategoryUsedException_whenCategoryIsUsed() {
        // Given
        var categoryId = 1L;
        var category = getCategory();
        var operation = getOperation(category, OperationType.PROFIT, 100);
        user.getWallet().setOperations(List.of(operation));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);

        // When & Then
        assertThrows(CategoryUsedException.class, () -> sut.deleteCategory(categoryId));
    }

    @Test
    @DisplayName("Смогли удалить категорию если она не используется")
    void delete_shouldDeleteCategoryCategory_whenCategoryIsNotUsed() {
        // Given
        var category = getCategory();
        user.getWallet().setOperations(List.of());
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);

        // When
        sut.deleteCategory(category.getId());

        // Then
        verify(categoryRepository).deleteById(category.getId());
    }

    @Test
    @DisplayName("Получили категорию по ид если она есть")
    void fetchCategory_shouldReturnCategory_whenCategoryExists() {
        // Given
        var category = getCategory();
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // When
        var result = sut.fetchCategory(category.getId());

        // Then
        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
    }

    @Test
    @DisplayName("Обработали ситуацию когда пытаемся получить категорию которой нет")
    void fetchCategory_shouldThrowNotFoundException_whenCategoryDoesNotExist() {
        // Given
        var categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundCategoryException.class, () -> sut.fetchCategory(categoryId));
    }

    @Test
    @DisplayName("Получили корректные итоги по категории")
    void getCurrentSumByCategory_shouldReturnCorrectSum() {
        // Given
        var category = getCategory();
        var operation1 = getOperation(category, OperationType.EXPENSE, 50);
        var operation2 = getOperation(category, OperationType.EXPENSE, 30);
        user.getWallet().setOperations(Arrays.asList(operation1, operation2));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);

        // When
        var result = sut.getCurrentSumByCategory(category, OperationType.EXPENSE);

        // Then
        assertEquals(BigDecimal.valueOf(80), result);
    }

    @Test
    @DisplayName("Получает сумму как разницу между доходами и расходами по категории")
    void getCurrentBalanceByCategory_shouldReturnCorrectBalance() {
        // Given
        var category = getCategory();
        var operation1 = getOperation(category, OperationType.PROFIT, 100);
        var operation2 = getOperation(category, OperationType.EXPENSE, 30);
        user.getWallet().setOperations(Arrays.asList(operation1, operation2));
        when(authFetcher.getCurrentAuthUser()).thenReturn(user);

        // When
        var result = sut.getCurrentBalanceByCategory(category);

        // Then
        assertEquals(BigDecimal.valueOf(70), result);
    }

    private Category getCategory() {
        var category = new Category();
        category.setId(1L);
        category.setName("Продукты");
        category.setLimiter(BigDecimal.valueOf(100));

        return category;
    }

    private Operation getOperation(Category category, OperationType profit, int val) {
        var operation = new Operation();
        operation.setCategory(category);
        operation.setOperationType(profit);
        operation.setAmount(BigDecimal.valueOf(val));

        return operation;
    }
}
