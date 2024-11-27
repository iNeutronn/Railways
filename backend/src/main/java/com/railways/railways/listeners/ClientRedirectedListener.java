package com.railways.railways.listeners;
import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientRedirectedUpdate;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.domain.client.ClientRedirected;
import com.railways.railways.events.ClientRedirectedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class ClientRedirectedListener {
    private final CommunicationSocketController socketController;

    public ClientRedirectedListener (CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onClientRedirected(ClientRedirectedEvent event) {
        ClientRedirected redirected = event.getClientRedirected();
        GenerationUpdateDTO update = new ClientRedirectedUpdate(redirected);
        socketController.sendUpdate(update);
    }
}
