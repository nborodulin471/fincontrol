package ru.fincontrol.service.exception;

/**
 * Исключение, возникает при попытке удаления используемой категории.
 *
 * @author Бородулин Никита Петрович.
 */
public class CategoryUsedException extends RuntimeException {
    public CategoryUsedException(String categoryName) {
        super(String.format("Категория %s используется в операциях",  categoryName));
    }
}
