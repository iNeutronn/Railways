package com.railways.railways.communication.DTO;

import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.Client;

public class ClientServedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a ClientServedUpdate.
     *
     *
     * @param serveRecord the client associated with the update
     */
    public ClientServedUpdate( ServeRecord serveRecord) {
        super(GenerationUpdateTypes.ClientServed, serveRecord);
    }
}
