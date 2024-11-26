package com.railways.railways.domain.client;

/**
 * Enum representing different client privileges with associated priority values.
 * The priority indicates the relative importance or urgency of the client based on their privilege.
 */
public enum PrivilegeEnum {
    DISABLED(1),
    WITHCHILD(3),
    WARVETERAN(2),
    DEFAULT(0);

    private final int priority;

    /**
     * Constructor for the PrivilegeEnum.
     * Associates a priority value with each privilege level.
     *
     * @param priority The priority level associated with the privilege.
     */
    PrivilegeEnum(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the priority value associated with the privilege level.
     * The higher the priority, the more urgent or important the client is.
     *
     * @return The priority of the privilege.
     */
    public int getPriority() {
        return priority;
    }

}
