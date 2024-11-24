package com.railways.railways.events;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import org.springframework.context.ApplicationEvent;

public class ClientCreatedEvent extends ApplicationEvent {
    private ClientCreated clientCreated;

    public ClientCreatedEvent(Object source, ClientCreated clientCreated) {
        super(source);
        this.clientCreated = clientCreated;
    }

    public  ClientCreated getClientCreated() {
        return clientCreated;
    }
}