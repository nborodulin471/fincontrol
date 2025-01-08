package ru.fincontrol.model.dto;

import lombok.Data;
import ru.fincontrol.model.entity.Category;
import ru.fincontrol.model.entity.Operation;
import ru.fincontrol.model.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Модель для отображения операций.
 *
 * @author Бородулин Никита Петрович.
 */
@Data
public class OperationDto {

    private long id;
    private Instant createdAt;
    private Category category;
    private BigDecimal amount;
    private OperationType operationType;
    private boolean exceededLimit;

    public static OperationDto map(Operation operation) {
        var operationDto = new OperationDto();
        operationDto.setId(operation.getId());
        operationDto.setCreatedAt(operation.getCreatedAt());
        operationDto.setCategory(operation.getCategory());
        operationDto.setAmount(operation.getAmount());
        operationDto.setOperationType(operation.getOperationType());

        return operationDto;
    }
}
