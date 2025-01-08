package ru.fincontrol.util;

import ru.fincontrol.model.entity.Operation;
import ru.fincontrol.model.OperationType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс утилит для работы с операциями.
 */
public class OperationsUtil {

    private OperationsUtil() {
    }

    /**
     * Получает сумму по типу трат.
     */
    public static BigDecimal getSum(List<Operation> operations, OperationType type) {
        return operations.stream()
                .filter(operation -> operation.getOperationType().equals(type))
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
