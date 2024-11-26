package com.railways.railways.communication.DTO;

import java.io.Serializable;

/**
 * Represents an abstract update to be sent to clients.
 *
 * The {@code GenerationUpdateDTO} class serves as a base for all types of updates.
 * It encapsulates the type of update and the associated data. This structure ensures
 * proper serialization and deserialization for network communication or storage.
 *
 * Subclasses of this class define specific types of updates and their corresponding
 * data structure.
 *
 * @implNote This class implements {@link Serializable} to allow instances to be
 * serialized for transmission or storage.
 *
 * @see GenerationUpdateTypes
 */
public abstract class GenerationUpdateDTO implements Serializable {

    /**
     * The type of the update.
     *
     * Specifies the type of update being sent. The value is defined by the
     * {@link GenerationUpdateTypes} enumeration.
     */
    public GenerationUpdateTypes type;

    /**
     * The data associated with the update.
     *
     * Contains the payload of the update, which can vary depending on the type of update.
     * The data is stored as an {@code Object} to allow flexibility in representing
     * various kinds of information.
     */
    public Object data;

    /**
     * Constructs an instance of {@code GenerationUpdateDTO}.
     *
     * @param type the {@link GenerationUpdateTypes} representing the type of the update
     * @param data the data associated with the update, represented as an {@code Object}
     */
    public GenerationUpdateDTO(GenerationUpdateTypes type, Object data) {
        this.type = type;
        this.data = data;
    }

}
