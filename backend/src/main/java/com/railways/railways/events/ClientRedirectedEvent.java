package com.railways.railways.events;

import com.railways.railways.domain.client.ClientRedirected;
import org.springframework.context.ApplicationEvent;

public class ClientRedirectedEvent extends ApplicationEvent {
    private final ClientRedirected clientRedirected;

    public ClientRedirectedEvent(Object source, ClientRedirected clientRedirected) {
        super(source);
        this.clientRedirected = clientRedirected;
    }

    public ClientRedirected getClientRedirected() {
        return clientRedirected;
    }

}
