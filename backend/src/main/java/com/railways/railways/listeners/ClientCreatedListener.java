package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientCreatedUpdate;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.communication.DTO.GenerationUpdateTypes;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.events.ClientCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClientCreatedListener {

    private final CommunicationSocketController socketController;

    public ClientCreatedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onClientCreated(ClientCreatedEvent event) {
        ClientCreated clientCreated = event.getClientCreated();

        GenerationUpdateDTO update = new ClientCreatedUpdate(clientCreated);
        socketController.sendUpdate(update);
    }
}