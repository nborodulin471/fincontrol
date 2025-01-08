package ru.fincontrol.model;

import lombok.Getter;

/**
 * Перечисление, которое отражает типы операций.
 *
 * @author Бородулин Никита Петрович.
 */
@Getter
public enum OperationType {
    PROFIT("Доход"),
    EXPENSE("Расход");

    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public static OperationType valueByName(String name) {
        for (OperationType value : OperationType.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
