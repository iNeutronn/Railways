package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.GenerationUpdateDTO;
import com.railways.railways.communication.GenerationUpdateTypes;
import com.railways.railways.domain.client.Client;
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
        Client client = event.getClient();
        GenerationUpdateDTO update = new GenerationUpdateDTO() {
            @Override
            public GenerationUpdateTypes getType() {
                return GenerationUpdateTypes.ClientCreated;
            }

            @Override
            public Object getData() {
                return client;
            }
        };

        socketController.sendUpdate(update);
    }
}