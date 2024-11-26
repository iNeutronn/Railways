package com.railways.railways.communication.DTO;

import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.Client;

/**
 * Represents an update that is triggered when a client has been served.
 *
 * This class extends {@link GenerationUpdateDTO} and encapsulates the details
 * of the client service event, including its type and associated data.
 * It uses the {@link GenerationUpdateTypes#ClientServed} type to indicate the
 * nature of the update.
 *
 * @see GenerationUpdateDTO
 * @see GenerationUpdateTypes
 * @see ServeRecord
 */
public class ClientServedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a {@code ClientServedUpdate}.
     *
     * @param serveRecord the {@link ServeRecord} object associated with this update,
     *                    containing details about the served client and the service event.
     */
    public ClientServedUpdate( ServeRecord serveRecord) {
        super(GenerationUpdateTypes.ClientServed, serveRecord);
    }
}
