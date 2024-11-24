package com.railways.railways.communication.DTO;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;

public class ClientCreatedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a ClientCreatedUpdate.
     *
     *
     * @param clientCreated the client associated with the update
     */
    public ClientCreatedUpdate(ClientCreated clientCreated) {
        super(GenerationUpdateTypes.ClientCreated, clientCreated);
    }
}
