package ru.fincontrol.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.dao.CategoryRepository;
import ru.fincontrol.model.entity.Category;
import ru.fincontrol.model.OperationType;
import ru.fincontrol.service.exception.CategoryUsedException;
import ru.fincontrol.service.exception.NotFoundCategoryException;

import java.math.BigDecimal;
import java.util.List;

import static ru.fincontrol.util.OperationsUtil.getSum;

/**
 * Сервис, который отвечает за работу с категориями.
 *
 * @author Бородулин Никита Петрович.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthFetcher authFetcher;

    public List<Category> findAllForWallet() {
        return authFetcher.getCurrentAuthUser().getWallet().getCategories();
    }

    /**
     * Добавляет новую категорию.
     */
    public Category addCategory(String name, BigDecimal limit) {
        var category = new Category();
        category.setName(name);
        category.setLimiter(limit);
        category.setWallet(authFetcher.getCurrentAuthUser().getWallet());

        return categoryRepository.save(category);
    }

    /**
     * Удаляет категорию.
     */
    public void deleteCategory(long id) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundCategoryException(id));

        var operationsForCategory = authFetcher.getCurrentAuthUser().getWallet().getOperations().stream()
                .filter(operation -> operation.getCategory().equals(category))
                .toList();

        if (!operationsForCategory.isEmpty()){
            throw new CategoryUsedException(category.getName());
        }

        categoryRepository.deleteById(id);
    }

    /**
     * Получает категорию по ее идентификатору.
     */
    public Category fetchCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundCategoryException(id));
    }

    /**
     * Получает общую сумму по категории и типу операций.
     */
    public BigDecimal getCurrentSumByCategory(Category category, OperationType type) {
        var operations = authFetcher.getCurrentAuthUser().getWallet().getOperations().stream()
                .filter(operation -> operation.getCategory().equals(category))
                .toList();

        return getSum(operations, type);
    }

    /**
     * Получает сумму как разницу между доходами и расходами по категории.
     */
    public BigDecimal getCurrentBalanceByCategory(Category category) {
        var operations = authFetcher.getCurrentAuthUser().getWallet().getOperations().stream()
                .filter(operation -> operation.getCategory().equals(category))
                .toList();

        return getSum(operations, OperationType.PROFIT).subtract(getSum(operations, OperationType.EXPENSE));
    }

}
