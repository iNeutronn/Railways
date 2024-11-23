package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientCreatedUpdate;
import com.railways.railways.communication.DTO.ClientServedUpdate;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.communication.DTO.GenerationUpdateTypes;
import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.Client;
import com.railways.railways.events.ClientCreatedEvent;
import com.railways.railways.events.ClientServedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClientServedListener {

    private final CommunicationSocketController socketController;

    public ClientServedListener (CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onClientServed(ClientServedEvent event) {
        ServeRecord record = event.getServeRecord();
        GenerationUpdateDTO update = new ClientServedUpdate(record);
        socketController.sendUpdate(update);
    }
}