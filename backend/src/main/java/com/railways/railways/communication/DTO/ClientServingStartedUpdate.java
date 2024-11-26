package com.railways.railways.communication.DTO;

public class ClientServingStartedUpdate extends GenerationUpdateDTO{
    public ClientServingStartedUpdate(ClientServingStartedData data) {
        super(GenerationUpdateTypes.ClientServingStarted, data);
    }
}
