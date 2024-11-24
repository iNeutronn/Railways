package com.railways.railways.communication.DTO;

import java.io.Serializable;

/**
 * GenerationUpdateDTO is an abstract class that represents an update to be sent to clients.
 * It contains public fields for the type of the update and the data associated with it.
 * This structure ensures proper serialization and deserialization.
 */
public abstract class GenerationUpdateDTO implements Serializable {

    /**
     * The type of the update.
     */
    public GenerationUpdateTypes type;

    /**
     * The data associated with the update.
     */
    public Object data;

    /**
     * Constructor for creating a GenerationUpdateDTO.
     *
     * @param type the type of the update
     * @param data the data associated with the update
     */
    public GenerationUpdateDTO(GenerationUpdateTypes type, Object data) {
        this.type = type;
        this.data = data;
    }
}
