package com.railways.railways.events;

import com.railways.railways.domain.client.Client;
import org.springframework.context.ApplicationEvent;

public class ClientCreatedEvent extends ApplicationEvent {
    private final Client createdClient;

    public ClientCreatedEvent(Object source, Client createdClient) {
        super(source);
        this.createdClient = createdClient;
    }

    public Client getCreatedClient() {
        return createdClient;
    }
}
