package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientServedUpdate;
import com.railways.railways.communication.DTO.ClientServingStartedUpdate;
import com.railways.railways.events.ClientServingStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClientServingStartedLisener {
    private final CommunicationSocketController socketController;

    public ClientServingStartedLisener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onClientServingStarted(ClientServingStartedEvent event) {
        var data = event.getClientServingStartedData();
        ClientServingStartedUpdate update = new ClientServingStartedUpdate(data);
        socketController.sendUpdate(update);
    }
}
