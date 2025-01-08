package ru.fincontrol.model.entity;

public enum OperationType {
    PROFIT("Доход"),
    EXPENSE("Расход");

    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
