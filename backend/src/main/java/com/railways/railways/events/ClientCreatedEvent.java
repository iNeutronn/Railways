package com.railways.railways.events;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import org.springframework.context.ApplicationEvent;

/**
 * Event representing the creation of a new client in the system.
 * It extends Spring's ApplicationEvent for use in an event-driven system.
 */
public class ClientCreatedEvent extends ApplicationEvent {
    private ClientCreated clientCreated;

    /**
     * Constructs a new ClientCreatedEvent.
     *
     * @param source the object that published the event (usually 'this')
     * @param clientCreated details about the created client
     */
    public ClientCreatedEvent(Object source, ClientCreated clientCreated) {
        super(source);
        this.clientCreated = clientCreated;
    }

    /**
     * Gets the details of the created client.
     *
     * @return an instance of ClientCreated containing client details
     */
    public  ClientCreated getClientCreated() {
        return clientCreated;
    }
}