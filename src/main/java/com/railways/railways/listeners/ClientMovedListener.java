package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.GenerationUpdateDTO;
import com.railways.railways.communication.GenerationUpdateTypes;
import com.railways.railways.domain.client.Client;
import com.railways.railways.events.ClientMovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClientMovedListener {

    private final CommunicationSocketController socketController;

    public ClientMovedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onClientUpdated(ClientMovedEvent event) {
        Client client = event.getClient();
        GenerationUpdateDTO update = new GenerationUpdateDTO() {
            @Override
            public GenerationUpdateTypes getType() {
                return GenerationUpdateTypes.ClientMoved;
            }

            @Override
            public Object getData() {
                return client;
            }
        };

        socketController.sendUpdate(update);
    }
}
// TODO: add event ClientServedEvent