package ru.fincontrol.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.fincontrol.dao.OperationRepository;
import ru.fincontrol.model.entity.Operation;
import ru.fincontrol.model.OperationType;
import ru.fincontrol.service.exception.ExceededExpensesException;
import ru.fincontrol.service.exception.LimitException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

import static ru.fincontrol.util.OperationsUtil.getSum;

/**
 * Сервис, который отвечает за работу с операциями.
 *
 * @author Бородулин Никита Петрович.
 */
@Service
@RequiredArgsConstructor
public class OperationService {

    private final AuthFetcher authFetcher;
    private final OperationRepository operationRepository;
    private final CategoryService categoryService;

    /**
     * Добавляет новую операцию
     */
    public void addOperation(long categoryId, BigDecimal amount, String type) {
        var category = categoryService.fetchCategory(categoryId);

        Operation operation = new Operation();
        operation.setWallet(authFetcher.getCurrentAuthUser().getWallet());
        operation.setCategory(category);
        operation.setCreatedAt(Instant.now());
        operation.setOperationType(OperationType.valueByName(type));
        operation.setAmount(amount);

        operationRepository.save(operation);

        checkLimit(operation);
    }

    /**
     * Получает операции для авторизованного пользователя.
     */
    public Collection<Operation> getOperation() {
        var wallet = authFetcher.getCurrentAuthUser().getWallet();
        return wallet.getOperations();
    }

    /**
     * Проверяет не превышены ли лимиты по операции.
     */
    public void checkLimit(Operation operation) {
        var limit = operation.getCategory().getLimiter();
        var currentTotal = categoryService.getCurrentSumByCategory(operation.getCategory(), OperationType.EXPENSE);
        if (currentTotal.add(operation.getAmount()).compareTo(limit) > 0) {
            throw new LimitException(operation.getCategory());
        }

        var expense = getCurrentSum(OperationType.EXPENSE);
        var profit = getCurrentSum(OperationType.PROFIT);
        if (operation.getOperationType().equals(OperationType.EXPENSE)) {
            expense = expense.add(operation.getAmount());
        } else if (operation.getOperationType().equals(OperationType.PROFIT)) {
            profit = profit.add(operation.getAmount());
        }

        if (expense.compareTo(profit) > 0) {
            throw new ExceededExpensesException();
        }
    }

    /**
     * Получает сумму по типу операций
     */
    public BigDecimal getCurrentSum(OperationType type) {
        var operations = authFetcher.getCurrentAuthUser().getWallet().getOperations().stream().toList();
        return getSum(operations, type);
    }

    /**
     * Удаляет операцию.
     */
    public void deleteOperation(long id) {
        operationRepository.deleteById(id);
    }
}
