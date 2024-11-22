package com.railways.railways.events;

import com.railways.railways.domain.client.Client;
import org.springframework.context.ApplicationEvent;

public class ClientMovedEvent extends ApplicationEvent {
    private final Client client;

    public ClientMovedEvent(Object source, Client client) {
        super(source);
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}