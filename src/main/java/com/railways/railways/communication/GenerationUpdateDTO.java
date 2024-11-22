package com.railways.railways.communication;

/**
 * GenerationUpdateDTO is an interface that represents an update to be sent to clients.
 * It provides methods to get the type of the update and the data associated with the update.
 */
public interface GenerationUpdateDTO {

    /**
     * Gets the type of the update.
     *
     * @return the type of the update
     */
    GenerationUpdateTypes getType();

    /**
     * Gets the data associated with the update.
     *
     * @return the data associated with the update
     */
    Object getData();
}