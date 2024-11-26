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

/**
 * Listener for handling ClientCreatedEvent events.
 */
@Component
public class ClientCreatedListener {
    private final CommunicationSocketController socketController;

    /**
     * Constructs a ClientCreatedListener with the provided socket controller.
     *
     * @param socketController the socket controller used to send updates
     */
    public ClientCreatedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    /**
     * Handles the ClientCreatedEvent.
     * Converts the client creation data into a DTO and sends it via the socket controller.
     *
     * @param event the event containing client creation details
     */
    @EventListener
    public void onClientCreated(ClientCreatedEvent event) {
        ClientCreated clientCreated = event.getClientCreated();

        GenerationUpdateDTO update = new ClientCreatedUpdate(clientCreated);
        socketController.sendUpdate(update);
    }
}