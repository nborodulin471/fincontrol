package ru.fincontrol.service.exception;

public class CategoryUsedException extends RuntimeException {
    public CategoryUsedException(long id) {
        super("Не удалось найти категорию с id " + id);
    }
}
