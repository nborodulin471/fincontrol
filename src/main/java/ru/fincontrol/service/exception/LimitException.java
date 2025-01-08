package ru.fincontrol.service.exception;

import ru.fincontrol.model.entity.Category;

/**
 * Исключение, возникает при превышении лимитов по категориям.
 *
 * @author Бородулин Никита Петрович.
 */
public class LimitException extends RuntimeException {

    public LimitException(Category category) {
        super("Превышен лимит по категории " + category.getName());
    }
}
