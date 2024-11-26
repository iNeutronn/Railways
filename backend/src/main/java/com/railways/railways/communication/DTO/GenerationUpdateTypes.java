package com.railways.railways.communication.DTO;

/**
 * Enumeration representing the various types of updates that can be sent to clients.
 *
 * This enum defines different update types that indicate specific events or changes
 * in the system, such as the creation of a client, a client being served, or updates
 * related to the queue. Each value corresponds to a specific kind of event, and the
 * system uses these types to determine the nature of the update being processed.
 *
 * @see GenerationUpdateDTO
 */
public enum GenerationUpdateTypes {
    /**
     * Represents an update indicating that a client has been created.
     */
    ClientCreated,
    /**
     * Represents an update indicating that a client has been served.
     */
    ClientServed,
    /**
     * Represents an update indicating that the queue has been updated.
     */
    QueueUpdated,
    ClientReachedQueue,
    /**
     * Represents an update indicating that a client has been transferred to a different queue.
     */
    QueueTransfered,
    ClientServingStarted
}
