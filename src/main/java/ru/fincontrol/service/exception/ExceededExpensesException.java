package ru.fincontrol.service.exception;

/**
 * Исключение, возникает при превышении расходов над доходами.
 *
 * @author Бородулин Никита Петрович.
 */
public class ExceededExpensesException extends RuntimeException {

    public ExceededExpensesException() {
        super("Зафиксировано превышение расходов над доходами. Пожалуйста, перейдите на главную страницу для дальнейших действий.");
    }
}
