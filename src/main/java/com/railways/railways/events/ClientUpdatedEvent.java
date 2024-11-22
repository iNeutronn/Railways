package com.railways.railways.events;

import com.railways.railways.domain.client.Client;
import org.springframework.context.ApplicationEvent;

public class ClientUpdatedEvent extends ApplicationEvent {
    private final Client client;

    public ClientUpdatedEvent(Object source, Client client) {
        super(source);
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}