package ru.fincontrol.service.exception;

/**
 * Исключение, возникает при попытке получить категории которой нет.
 *
 * @author Бородулин Никита Петрович.
 */
public class NotFoundCategoryException extends RuntimeException {
    public NotFoundCategoryException(long id) {
        super("Не удалось найти категорию с id " + id);
    }
}
