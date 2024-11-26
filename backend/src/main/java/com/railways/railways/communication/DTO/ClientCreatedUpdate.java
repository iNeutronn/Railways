package com.railways.railways.communication.DTO;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;

/**
 * Represents an update that is triggered when a client is created.
 *
 * This class extends {@link GenerationUpdateDTO} and encapsulates the details
 * of the client creation event, including its type and associated data.
 * It uses the {@link GenerationUpdateTypes#ClientCreated} type to indicate the
 * nature of the update.
 *
 * @see GenerationUpdateDTO
 * @see GenerationUpdateTypes
 * @see ClientCreated
 */
public class ClientCreatedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a {@code ClientCreatedUpdate}.
     *
     * @param clientCreated the {@link ClientCreated} object associated with this update,
     *                      containing details about the created client.
     */
    public ClientCreatedUpdate(ClientCreated clientCreated) {
        super(GenerationUpdateTypes.ClientCreated, clientCreated);
    }
}
