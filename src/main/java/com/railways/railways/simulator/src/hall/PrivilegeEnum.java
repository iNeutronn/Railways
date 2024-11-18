package com.railways.railways.simulator.src.hall;


public enum PrivilegeEnum {
    DISABLED(1),
    WITHCHILD(3),
    WARVETERAN(2);

    private final int priority;

    // Конструктор для enum
    PrivilegeEnum(int priority) {
        this.priority = priority;
    }

    // Метод для отримання пріоритету
    public int getPriority() {
        return priority;
    }

}
