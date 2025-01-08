package ru.fincontrol.model.dto;


import ru.fincontrol.model.entity.Category;

import java.math.BigDecimal;

/**
 * Модель для отображения категорий.
 *
 * @author Бородулин Никита Петрович.
 */
public record CategoryDto(long id, String name, BigDecimal limiter) {

    public static CategoryDto map(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getLimiter());
    }

}
