package com.railways.railways.communication.DTO;

import com.railways.railways.domain.client.Client;

public class ClientCreatedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a ClientCreatedUpdate.
     *
     *
     * @param newClient the client associated with the update
     */
    public ClientCreatedUpdate( Client newClient) {
        super(GenerationUpdateTypes.ClientCreated, newClient);
    }
}
