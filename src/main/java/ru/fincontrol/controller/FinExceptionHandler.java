package ru.fincontrol.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import ru.fincontrol.service.exception.CategoryUsedException;
import ru.fincontrol.service.exception.ExceededExpensesException;
import ru.fincontrol.service.exception.LimitException;

/**
 * Обрабатывает ошибки возникшие в ходе работы приложения.
 *
 * @author Бородулин Никита Петрович.
 */
@Slf4j
@ControllerAdvice
public class FinExceptionHandler {

    public static final String ATTRIBUTE_NAME_MESSAGE = "message";
    public static final String ERROR = "error";

    /**
     * Ошибка превышения лимита.
     */
    @ExceptionHandler(LimitException.class)
    public String handleLimitException(Model model, LimitException e) {
        log.debug(e.getMessage(), e);
        model.addAttribute(ATTRIBUTE_NAME_MESSAGE, e.getMessage());

        return ERROR;
    }

    /**
     * Ошибка превышения расходов над доходами.
     */
    @ExceptionHandler(ExceededExpensesException.class)
    public String handleExceededExpensesException(Model model, ExceededExpensesException e) {
        log.debug(e.getMessage(), e);
        model.addAttribute(ATTRIBUTE_NAME_MESSAGE, e.getMessage());

        return ERROR;
    }

    /**
     * Ошибка при попытке удалить используемую категорию.
     */
    @ExceptionHandler(CategoryUsedException.class)
    public String handleCategoryUsedException(Model model, CategoryUsedException e) {
        log.debug(e.getMessage(), e);
        model.addAttribute(ATTRIBUTE_NAME_MESSAGE, e.getMessage());

        return ERROR;
    }

    /**
     * Прочие ошибки.
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(Model model, RuntimeException e) {
        log.debug(e.getMessage(), e);
        model.addAttribute(ATTRIBUTE_NAME_MESSAGE, "Возникла ошибка: " + e.getMessage());

        return ERROR;
    }
}


